package com.petstore.utils;

import org.stringtemplate.v4.ST;

import java.util.*;
import java.util.function.UnaryOperator;

public class ConfigurationParamUtils {

    private ConfigurationParamUtils(){}

    private static final  String[] dbConnPipelineParameters={"<type>_USERNAME", "<type>_PASSWORD", "<type>_HOST", "<type>_PORT", "<type>_BASE"};
    private static final  String[] dbConnLocalParameters={"<type>.username", "<type>.password", "<type>.host", "<type>.port", "<type>.base"};

    public static Map<String, Object> loadEnviromentalValues(String strDatabaseType) {
        Map<String, String> valuesMap;
        DataBaseType dbType = DataBaseType.valueOf(strDatabaseType);
        Optional<Map<String,String>> opValuesEnvMap = createConfigMapEnvParams(dbType);
        Map<String,Object> configMap = new HashMap<>();

        String url;
        if(opValuesEnvMap.isPresent()){
            valuesMap = normalizeNames(opValuesEnvMap.get(), "_");
        }else{
            PropertiesUtils propertiesUtils = new PropertiesUtils();
            Optional<Properties> opProperties = propertiesUtils.getPropValues();
            valuesMap =  normalizeNames(opProperties
                    .map(properties ->ConfigurationParamUtils.createConfigMapPropertiesParams(properties, dbType))
                    .orElseGet(HashMap::new), "\\.");
        }

        if(checkNulls(valuesMap)){
            throw new IllegalArgumentException("Valores nulos para la creación de la conexion a la base");
        }else {
            url = createURL(dbType.getJdbcUrl(), valuesMap.get("host"),
                    valuesMap.get("port"), valuesMap.get("base"));
        }
        configMap.put("username",valuesMap.get("username"));
        configMap.put("password",valuesMap.get("password"));
        configMap.put("url",url);
        configMap.put("driverClassName",dbType.getDriver());
        configMap.put("database", valuesMap.get("base"));
        return configMap;
    }

    private static Map<String, String> normalizeNames(Map<String, String> stringStringMap, String token) {
        Map<String, String> normalizedNames = new HashMap<>();

        for(Map.Entry<String, String> entry:stringStringMap.entrySet()){
            String[] splittedValues = entry.getKey().toLowerCase().split(token);
            normalizedNames.put(splittedValues[1], entry.getValue());
        }
        return normalizedNames;
    }

    private static boolean checkNulls(Map<String, String> valuesMap) {
        if(valuesMap.isEmpty()) {
            return true;
        }else{
            for (String parameter : valuesMap.values()) {
                if (Objects.isNull(parameter)) {
                    return true;
                }
            }
        }
        return false;
    }

    private static Optional<Map<String,String>> createConfigMapEnvParams(DataBaseType dbtype) {
        Map<String,String> valuesMap = new HashMap<>();
        String[] finalParameters = getParametersWithDbType(dbConnPipelineParameters, dbtype, value -> value);
        for(String parameter:finalParameters){
            if(Objects.nonNull(System.getenv(parameter))) {
                valuesMap.put(parameter, System.getenv(parameter));
            }else{
                return Optional.empty();
            }
        }
        return Optional.of(valuesMap);
    }
    private static Map<String,String> createConfigMapPropertiesParams(Properties properties, DataBaseType dbtype) {
        Map<String,String> valuesMap = new HashMap<>();
        String[] finalParameters = getParametersWithDbType(dbConnLocalParameters, dbtype, String::toLowerCase);
        for(String parameter:finalParameters){
            String value = properties.getProperty(parameter);
            valuesMap.put(parameter, value);
        }
        return valuesMap;
    }

    private static String[] getParametersWithDbType(String[] dbConnParameters, DataBaseType dbtype, UnaryOperator<String> modifier) {
        String [] finalParameters = new String[5];
        for(int i = 0; i < dbConnParameters.length; i++){
            ST strConfiguration = new ST(dbConnParameters[i]);
            strConfiguration.add("type", modifier.apply(dbtype.name()));
            finalParameters[i] = strConfiguration.render();
        }
        return finalParameters;
    }

    private static String createURL(String urlTemplate, String host, String port, String dbName) {
        ST stUrl = new ST(urlTemplate);
        stUrl.add("host",host);
        stUrl.add("port", port);
        stUrl.add("database", dbName);
        return stUrl.render();
    }
}
