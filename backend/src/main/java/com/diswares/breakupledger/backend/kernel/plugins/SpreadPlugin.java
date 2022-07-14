package com.diswares.breakupledger.backend.kernel.plugins;


import plugin.TypePlugin;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author jnwang
 * @date 2022/03/23 16:30
 **/
public class SpreadPlugin {

    private final List<TypePlugin<?,?>> plugins = new LinkedList<>();

    private final Map<Class<?>,TypePlugin<?,?>> localPlugins = new LinkedHashMap<>();

    public List<TypePlugin<?, ?>> getPlugins() {
        return plugins;
    }

    public Map<Class<?>, TypePlugin<?, ?>> getLocalPlugins() {
        return localPlugins;
    }

    public SpreadPlugin spread(TypePlugin<?,?> plus){
        plugins.add(plus);
        return this;
    }

    public SpreadPlugin assignSpread(Class<?> aClass,TypePlugin<?,?> plus){
        localPlugins.put(aClass,plus);
        return this;
    }
}
