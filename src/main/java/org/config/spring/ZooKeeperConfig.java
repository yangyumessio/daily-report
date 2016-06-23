package org.config.spring;

/**
 * Created by yangyu on 2016/6/2.
 */
import org.apache.curator.framework.CuratorFramework;
import org.apache.zookeeper.data.Stat;


public class ZooKeeperConfig implements Config {
    public byte[] getConfig(String path, String connect, String port) throws Exception {
        CuratorFramework client = ZooKeeperFactory.get(connect,port);
        if (!exists(client, path)) {
            throw new RuntimeException("Path " + path + " does not exists.");
        }
        return client.getData().forPath(path);
    }

    private boolean exists(CuratorFramework client, String path) throws Exception {
        Stat stat = client.checkExists().forPath(path);
        return !(stat == null);
    }

}
