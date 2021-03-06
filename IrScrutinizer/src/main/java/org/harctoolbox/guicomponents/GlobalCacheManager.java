/*
 Copyright (C) 2013 Bengt Martensson.

 This program is free software: you can redistribute it and/or modify
 it under the terms of the GNU General Public License as published by
 the Free Software Foundation; either version 3 of the License, or (at
 your option) any later version.

 This program is distributed in the hope that it will be useful, but
 WITHOUT ANY WARRANTY; without even the implied warranty of
 MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 General Public License for more details.

 You should have received a copy of the GNU General Public License along with
 this program. If not, see http://www.gnu.org/licenses/.
 */
package org.harctoolbox.guicomponents;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import org.harctoolbox.harchardware.beacon.AmxBeaconListener;
import org.harctoolbox.harchardware.ir.GlobalCache;

/**
 * This class runs a AmxBeaconListener for GlobalCache's in a thread.
 * This is singleton class, since it is simply not sensible to run more than one instance on the host.
 * There are functions for adding and removing GlobalCache units not adhering to the AMX Beacon protocol
 * ("manual GlobalCaches"). PropertyChangeSupport is provided.
 *
 * Initial selections is to be handled by adding a manual GlobalCache.
 * If later an automatic entry for the same IP appears, the manual entry will be automatically removed.
 */
public final class GlobalCacheManager {
    private boolean debug = false;
    private static final GlobalCacheManager instance = new GlobalCacheManager();
    private AmxBeaconListener beaconListener = null;
    private ArrayList<InetAddress> automaticGlobalCaches = new ArrayList<InetAddress>();
    private ArrayList<String> automaticGlobalCacheTypes = new ArrayList<String>();
    private final ArrayList<InetAddress> manualGlobalCaches = new ArrayList<InetAddress>();

    private final PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

    public final static String PROP_GCMANAGER_NAME = "globalcacheManager";

    private int sillyHashCode() {
        return automaticGlobalCaches.hashCode() + 1000*manualGlobalCaches.hashCode();
    }

    private class Callbacker implements AmxBeaconListener.Callback {

        Callbacker() {
        }

        @Override
        public void func(HashMap<InetAddress, AmxBeaconListener.Node> nodes) {
            int oldValue = sillyHashCode();
            automaticGlobalCaches = new ArrayList<InetAddress>();
            automaticGlobalCacheTypes = new ArrayList<String>();
            //ipAddressMap = new LinkedHashMap<InetAddress, Integer>();
            for (Entry<InetAddress, AmxBeaconListener.Node> node : nodes.entrySet()) {
                automaticGlobalCaches.add(node.getKey());
                automaticGlobalCacheTypes.add(node.getValue().get("-Model"));
                //ipAddressMap.put(node.getKey(), oldValue)
                removeManualGlobalCache(node.getKey());
            }
            int newValue = sillyHashCode();
            propertyChangeSupport.firePropertyChange(PROP_GCMANAGER_NAME, oldValue, newValue);
        }
    }

    /**
     * @return the instance
     */
    public static GlobalCacheManager getInstance() {
        return instance;
    }

    /**
     * @param debug
     */
    public void setDebug(boolean debug) {
        this.debug = debug;
        if (beaconListener != null)
           beaconListener.setDebug(debug);
    }

    public String getType(int index) {
        if (index < manualGlobalCaches.size())
            return "manually entered";
        return automaticGlobalCacheTypes.get(index - manualGlobalCaches.size());
    }

    public InetAddress getInetAddress(int index) {
        if (index < 0)
            return null;
        if (index < manualGlobalCaches.size())
            return manualGlobalCaches.get(index);
        return automaticGlobalCaches.get(index - manualGlobalCaches.size());
    }

    public int getIndex(InetAddress inetAddress) {
        for (int index = 0; index < manualGlobalCaches.size(); index++)
            if (manualGlobalCaches.get(index).equals(inetAddress))
                return index;

        for (int index = 0; index < automaticGlobalCaches.size(); index++)
            if (automaticGlobalCaches.get(index).equals(inetAddress))
                return index + manualGlobalCaches.size();

        return -1;
    }

    public boolean addManualGlobalCache(String ipName) throws IOException {
        return addManualGlobalCache(InetAddress.getByName(ipName));
    }

    public boolean addManualGlobalCache(InetAddress inetAddress) {
        if (inetAddress == null || manualGlobalCaches.contains(inetAddress) || automaticGlobalCaches.contains(inetAddress))
            return false;

        int oldValue = sillyHashCode();

        manualGlobalCaches.add(inetAddress);
        propertyChangeSupport.firePropertyChange(PROP_GCMANAGER_NAME, oldValue, sillyHashCode());
        return true;
    }

    public boolean removeManualGlobalCache(InetAddress inetAddress) {
        int oldValue = sillyHashCode();
        if (!manualGlobalCaches.contains(inetAddress))
            return false;
        manualGlobalCaches.remove(inetAddress);
        propertyChangeSupport.firePropertyChange(PROP_GCMANAGER_NAME, oldValue, sillyHashCode());
        return true;
    }

    public InetAddress[] getManual() {
        return manualGlobalCaches.toArray(new InetAddress[manualGlobalCaches.size()]);
    }

    public InetAddress[] getAutomatic() {
        return automaticGlobalCaches.toArray(new InetAddress[automaticGlobalCaches.size()]);
    }

    public String[] getAllNames() {
        String[] result = new String[manualGlobalCaches.size() + automaticGlobalCaches.size()];
        for (int i = 0; i < manualGlobalCaches.size(); i++)
            result[i] = manualGlobalCaches.get(i).getHostName();
        System.arraycopy(getAutomaticPrettyNames(), 0, result, manualGlobalCaches.size(), automaticGlobalCaches.size());
        return result;
    }

    public String[] getAutomaticPrettyNames() {
        String[] result = new String[automaticGlobalCaches.size()];
        for (int i = 0; i < result.length; i++)
            result[i] = automaticGlobalCacheTypes.get(i) + " (" + automaticGlobalCaches.get(i).getHostName() + ")";
        return result;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        propertyChangeSupport.removePropertyChangeListener(listener);
    }

    private GlobalCacheManager() {
        Callbacker callback = new Callbacker();
        beaconListener = GlobalCache.newListener(callback, debug);
    }
}
