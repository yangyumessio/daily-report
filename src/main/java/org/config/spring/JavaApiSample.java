package org.config.spring;

/**
 * Created by yangyu on 2016/6/13.
 */


import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.KeeperState;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;


public class JavaApiSample implements Watcher {

    private static final int SESSION_TIMEOUT = 30000;
  //  private static final String CONNECTION_STRING = "test.zookeeper.connection_string:2181";
    private static final String CONNECTION_STRING = "10.31.72.73:2181";
 // private static final String CONNECTION_STRING = "127.0.0.1:2181";
    private static final String ZK_PATH = "/yang";
    private static final String JDBC_DATA = "jdbc.driverClassName#com.mysql.jdbc.Driver#"
        + "jdbc.url#jdbc:mysql://10.31.73.48:3306/hue?user=hue_user&password=hue_test&useUnicode=true&characterEncoding=UTF8#"
        + "jdbc.username#hue_user#"
        + "jdbc.password#hue_test#"
        + "jdbc.initialPoolSize#10#"
        + "jdbc.minPoolSize#2#"
        + "jdbc.maxPoolSize#30#";
    private ZooKeeper zk = null;

    private CountDownLatch connectedSemaphore = new CountDownLatch( 1 );

    /**
     * 鍒涘缓ZK杩炴帴
     * @param connectString  ZK鏈嶅姟鍣ㄥ湴鍧�垪琛�
     * @param sessionTimeout   Session瓒呮椂鏃堕棿
     */
    public void createConnection( String connectString, int sessionTimeout ) {
        this.releaseConnection();
        try {
            zk = new ZooKeeper( connectString, sessionTimeout, this );
            connectedSemaphore.await();
        } catch ( InterruptedException e ) {
            System.out.println( "杩炴帴鍒涘缓澶辫触锛屽彂鐢�InterruptedException" );
            e.printStackTrace();
        } catch ( IOException e ) {
            System.out.println( "杩炴帴鍒涘缓澶辫触锛屽彂鐢�IOException" );
            e.printStackTrace();
        }
    }

    /**
     * 鍏抽棴ZK杩炴帴
     */
    public void releaseConnection() {
        if (  this.zk != null ) {
            try {
                this.zk.close();
            } catch ( InterruptedException e ) {
                // ignore
                e.printStackTrace();
            }
        }
    }

    /**
     *  鍒涘缓鑺傜偣
     * @param path 鑺傜偣path
     * @param data 鍒濆鏁版嵁鍐呭
     * @return
     */
    public boolean createPath( String path, String data ) {
        try {
            this.zk.create( path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT );
    //        System.out.println( "鑺傜偣鍒涘缓鎴愬姛, Path: "+ this.zk.create( path,data.getBytes(),Ids.OPEN_ACL_UNSAFE,CreateMode.EPHEMERAL )+ ", content: " + data );
        } catch ( KeeperException e ) {
            System.out.println( "鑺傜偣鍒涘缓澶辫触锛屽彂鐢烱eeperException" );
            e.printStackTrace();
        } catch ( InterruptedException e ) {
            System.out.println( "鑺傜偣鍒涘缓澶辫触锛屽彂鐢�InterruptedException" );
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 璇诲彇鎸囧畾鑺傜偣鏁版嵁鍐呭
     * @param path 鑺傜偣path
     * @return
     */
    public String readData( String path ) {
        try {
            System.out.println( "鑾峰彇鏁版嵁鎴愬姛锛宲ath锛�"+ path );
            return new String( this.zk.getData( path, false, null ) );
        } catch ( KeeperException e ) {
            System.out.println( "璇诲彇鏁版嵁澶辫触锛屽彂鐢烱eeperException锛宲ath: " + path  );
            e.printStackTrace();
            return "";
        } catch ( InterruptedException e ) {
            System.out.println( "璇诲彇鏁版嵁澶辫触锛屽彂鐢�InterruptedException锛宲ath: " + path  );
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 鏇存柊鎸囧畾鑺傜偣鏁版嵁鍐呭
     * @param path 鑺傜偣path
     * @param data  鏁版嵁鍐呭
     * @return
     */
    public boolean writeData( String path, String data ) {
        try {
            System.out.println( "鏇存柊鏁版嵁鎴愬姛锛宲ath锛"+ path + ", stat: " +
                    this.zk.setData( path, data.getBytes(), -1 ) );
        } catch ( KeeperException e ) {
            System.out.println( "鏇存柊鏁版嵁澶辫触锛屽彂鐢烱eeperException锛宲ath: " + path  );
            e.printStackTrace();
        } catch ( InterruptedException e ) {
            System.out.println( "鏇存柊鏁版嵁澶辫触锛屽彂鐢�InterruptedException锛宲ath: " + path  );
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 鍒犻櫎鎸囧畾鑺傜偣
     * @param path 鑺傜偣path
     */
    public void deleteNode( String path ) {
        try {
            this.zk.delete( path, -1 );
            System.out.println( "鍒犻櫎鑺傜偣鎴愬姛锛宲ath锛�"+ path );
        } catch ( KeeperException e ) {
            System.out.println( "鍒犻櫎鑺傜偣澶辫触锛屽彂鐢烱eeperException锛宲ath: " + path  );
            e.printStackTrace();
        } catch ( InterruptedException e ) {
            System.out.println( "鍒犻櫎鑺傜偣澶辫触锛屽彂鐢�InterruptedException锛宲ath: " + path  );
            e.printStackTrace();
        }
    }

    public static void main( String[] args ) {

        JavaApiSample sample = new JavaApiSample();
        sample.createConnection( CONNECTION_STRING, SESSION_TIMEOUT );
       // if ( sample.createPath( ZK_PATH, "鎴戞槸鑺傜偣鍒濆鍐呭" ) ) {
        sample.createPath( ZK_PATH, JDBC_DATA);
        System.out.println("鏁版嵁鍐呭: " + sample.readData(ZK_PATH) + "\n" );
         //   System.out.println( "鏁版嵁鍐呭: " + sample.readData( ZK_PATH ) + "\n" );
        //    sample.writeData( ZK_PATH, "鏇存柊鍚庣殑鏁版嵁" );
       //     System.out.println( "鏁版嵁鍐呭: " + sample.readData( ZK_PATH ) + "\n" );
       //     sample.deleteNode( ZK_PATH );
      //  }

       // sample.releaseConnection();
    }

    /**
     * 鏀跺埌鏉ヨ嚜Server鐨刉atcher閫氱煡鍚庣殑澶勭悊銆�
     */
    public void process( WatchedEvent event ) {
        System.out.println( "鏀跺埌浜嬩欢閫氱煡锛"+ event.getState() +"\n"  );
        if ( KeeperState.SyncConnected == event.getState() ) {
            connectedSemaphore.countDown();
        }

    }

}