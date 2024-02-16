package com.petstore.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ControlsExecutionParallelAgents {

    static Logger logger = Logger.getLogger(ControlsExecutionParallelAgents.class.getName());

    private ControlsExecutionParallelAgents() {
        throw new IllegalStateException("Utility class");
    }

    static List<String> allFeatures = new ArrayList<>();
    static EnvironmentConfig environmentConfig = new EnvironmentConfig();
    private static List<String> featuresList = new ArrayList<>();
    private static boolean parameterizedSegmentation;

    public static void featuresSegmentation() {
        final String FEATURE_NAME = "todos";
        String totalPe = environmentConfig.getVariable("SYSTEM_TOTALJOBSINPHASE");
        String Num = environmentConfig.getVariable("SYSTEM_JOBPOSITIONINPHASE");
        logger.log(Level.INFO, () -> "=====> Total : '" + totalPe + "' |  Num: '" + Num + "'");

        final String FEATURES_PATH = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "java"
                + File.separator + "com" + File.separator + "petstore" + File.separator + "features" + File.separator;

        allFeatures = listFilesByFolder(FEATURE_NAME, new File(FEATURES_PATH));

        if (valitateParalelExcecution(totalPe)) {
            List<String> pathsFeatureToRemove = getPathsFeatureToRemove(NumPET);
            if (!parameterizedSegmentation) {
                pathsFeatureToRemove.clear();
                pathsFeatureToRemove = getPathsFeatureToRemoveDefault(totalPe, NumPET);
            }
            removeFeatures(pathsFeatureToRemove);
        }
    }

    private static boolean valitateParalelExcecution(String totalPe) {
        totalPe = totalPe.equals("") ? "1" : totalPe;
        return Integer.parseInt(totalPe) > 1;
    }

    private static List<String> getPathsFeatureToRemove(String NumPET) {

        List<String> featuresToDelete = new ArrayList<>();

        for (String featurePath : allFeatures) {
            String data;
            boolean isPresentPet = false;
            try (BufferedReader bufferedReader = Files.newBufferedReader(Paths.get(featurePath), StandardCharsets.UTF_8)) {
                while ((data = bufferedReader.readLine()) != null) {
                    if (data.trim().contains(" @user")) parameterizedSegmentation = true;
                    isPresentPet = data.trim().contains(" @user" + NumPET);
                    if (isPresentPet) {
                        break;
                    }
                }
                if (!isPresentPet) {
                    featuresToDelete.add(featurePath);
                }
            } catch (IOException e) {
                throw new IllegalStateException("=====> ERROR al crear lista de features para eliminar, " + e.getMessage(), e);
            }
        }
        return featuresToDelete;
    }

    private static List<String> getPathsFeatureToRemoveDefault(String totalAgentsExecution, String agentNumberExcecution) {

        List<String> featuresPathToRemove = new ArrayList<>();
        double totalP = Double.parseDouble(totalAgentsExecution);
        int NumberP = Integer.parseInt(agentNumberExcecution);
        double totalFeatures = allFeatures.size();
        double asignedFeatures = totalFeatures / totalP;

        double asignedFeaturesRounded = Math.round(asignedFeatures);
        logger.log(Level.INFO, () -> "=====> Total Features: " + totalFeatures + " | asignedFeatures: " + asignedFeatures + " | asignedFeaturesRounded: " + asignedFeaturesRounded);


        if (asignedFeatures >= 1) {
            int numFeatureDesde = (int) ((NumberP * asignedFeaturesRounded) - asignedFeaturesRounded) + 1;
            int numFeatureHasta = Math.min((int) (NumberP * asignedFeaturesRounded), allFeatures.size());
            logger.log(Level.INFO, () -> "=====> numFeatureDesde: " + numFeatureDesde + " | numFeatureHasta: " + numFeatureHasta);

            for (int i = 0; i < allFeatures.size(); i++) {
                if (i < numFeatureDesde - 1 || i > numFeatureHasta - 1) {
                    if (NumberP == totalP && numFeatureHasta < allFeatures.size() && i >= numFeatureHasta) {
                        int featuresExtras = allFeatures.size() - numFeatureHasta;
                        logger.log(Level.INFO, () -> "=====> Features Extras para Ejecutar: " + featuresExtras);
                    } else {
                        featuresPathToRemove.add(allFeatures.get(i));
                    }
                }
            }
        } else {
            throw new IllegalStateException("ERROR: NO se puede realizar segmentacion de features ");
        }
        return featuresPathToRemove;
    }

    private static void removeFeatures(List<String> pathsFeatureToRemove) {
        logger.log(Level.INFO, () -> "=====> Total de features a borrar: " + pathsFeatureToRemove.size());
        pathsFeatureToRemove.forEach(feature -> logger.log(Level.INFO, () -> "Feature a borrar:" + feature));

        for (String featurePath : pathsFeatureToRemove) {
            try {
                Files.delete(Paths.get(featurePath));
            } catch (IOException e) {
                throw new IllegalStateException("=====> ERROR al eliminar feature, " + featurePath + " - " + e.getMessage(), e);
            }
        }
    }

    public static List<String> listFilesByFolder(String featureName, final File folder) {
        final String ALL_FEATURES = "todos";
        List<String> featuresListCopy;
        if (featureName.equalsIgnoreCase(ALL_FEATURES)) {
            for (final File fileOrFolder : Objects.requireNonNull(folder.listFiles())) {
                if (fileOrFolder.isDirectory()) {
                    listFilesByFolder(featureName, fileOrFolder);
                } else {
                    featuresList.add(fileOrFolder.getAbsolutePath());
                }
            }
        } else {
            featuresList = List.of(featureName.split(";"));
        }
        featuresListCopy = featuresList;
        return featuresListCopy;
    }

}