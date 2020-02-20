package org.dorax.network;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * 获取 ip 地址的工具类
 *
 * @author wuchunfu
 * @date 2020-02-08
 */
public class NetworkUtils {

    private static final Logger LOG = LoggerFactory.getLogger(NetworkUtils.class);

    /**
     * 获取主机名
     *
     * @return 主机名
     */
    public static String getLocalHostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            LOG.error("get local hostname failed. return local ip instead.", e);
            return getLocalAddress();
        }
    }

    /**
     * 获取本地IP地址
     *
     * @return 本地IP地址
     */
    public static String getLocalAddress() {
        try {
            final Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            final List<String> ipv4Result = new ArrayList<>();
            final List<String> ipv6Result = new ArrayList<>();
            while (interfaces.hasMoreElements()) {
                final NetworkInterface networkInterface = interfaces.nextElement();
                if (!networkInterface.isUp()) {
                    continue;
                }
                if (networkInterface.isVirtual()) {
                    continue;
                }
                final Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    final InetAddress address = addresses.nextElement();
                    if (!address.isLoopbackAddress()) {
                        if (address instanceof Inet6Address) {
                            ipv6Result.add(address.getHostAddress());
                        } else {
                            ipv4Result.add(address.getHostAddress());
                        }
                    }
                }
            }
            if (!ipv4Result.isEmpty()) {
                for (String ip : ipv4Result) {
                    if (ip.startsWith("127.0")) {
                        continue;
                    }
                    return ip;
                }
                return ipv4Result.get(ipv4Result.size() - 1);
            } else if (!ipv6Result.isEmpty()) {
                return ipv6Result.get(0);
            }
            return InetAddress.getLocalHost().getHostAddress();
        } catch (Exception e) {
            LOG.error("get local address failed", e);
        }
        return null;
    }

    /**
     * 验证 ip:port 地址是否有效
     *
     * @param address ip:port 地址
     * @return 是否有效
     */
    public static boolean isValid(String address) {
        try {
            final String[] s = address.split(":");
            new InetSocketAddress(s[0], Integer.parseInt(s[1]));
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void main(String[] args) {
        System.out.println("getLocalHostname: " + getLocalHostname());
        System.out.println("getLocalAddress: " + getLocalAddress());
        boolean valid = isValid("192.168.10.100:8080");
        System.out.println("isValid: " + valid);
    }
}
