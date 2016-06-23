package org.config.spring;

import org.apache.commons.lang.StringUtils;
import org.config.spring.utils.CyptoUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;
import java.io.UnsupportedEncodingException;
import java.util.Properties;
/**
 * Created by yangyu on 2016/6/2.
 */
public class ZooKeeperPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {
    public static final String PATH = "zoo.paths";
    public static final String ZK_ADDRESS = "zk_address";
    public static final String ZK_PORT = "zk_port";
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        try {
            fillCustomProperties(props);
            System.out.println(props);
            super.processProperties(beanFactoryToProcess, props);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void fillCustomProperties(Properties props) throws Exception {
        byte[] data = getData(props);
        fillProperties(props, data);
    }
    private void fillProperties(Properties props, byte[] data) throws UnsupportedEncodingException {
        String cfg = new String(data, "UTF-8");
        String cfgEncode = CyptoUtils.decode(null, cfg);
        if (StringUtils.isNotBlank(cfgEncode)) {
            String[] cfgItem = StringUtils.split(cfgEncode, "$");
            int length = cfgItem.length;
            for(int i=0; i < length; i=i+2) {
                props.put(cfgItem[i], cfgItem[i+1]);
            }
        }
    }
    private byte[] getData(Properties props) throws Exception {
        String path = props.getProperty(PATH);
        String connect = props.getProperty(ZK_ADDRESS);
        String[] cfgItem = StringUtils.split(connect, ",");
        String port = props.getProperty(ZK_PORT);
        Config config = new ZooKeeperConfig();
        return config.getConfig(path,cfgItem[0], port);
    }

}
