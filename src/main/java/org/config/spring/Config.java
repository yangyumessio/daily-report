package org.config.spring;

/**
 * Created by yangyu on 2016/6/2.
 */
public interface Config {

    byte[] getConfig(String path,String connect, String port) throws Exception;
}