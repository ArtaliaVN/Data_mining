package Data.Processing.DataMining.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Data.Processing.DataMining.Entity.DatasetEntity;
import weka.classifiers.Classifier;
import weka.core.Utils;

public class CommandLineService {
    private final Scanner scanner = new Scanner(System.in);
    private boolean isRunning = true;
    private String currentCommand = "";
    private DatasetEntity datasetEntity;
    private DatasetEntity workingEntity;
    private DatasetEntity testingEntity;
    private Classifier workingClassifier;

    private final DataProcessingService dataProcessingService;

    private final DataClassificationService dataClassificationService;

    private final DataTestingService dataTestingService;

    public CommandLineService(){
        this.dataClassificationService = new DataClassificationService();
        this.dataProcessingService = new DataProcessingService();
        this.dataTestingService = new DataTestingService();
    }

    public void cmdRunning() throws Exception{
        
        while(isRunning){
            if(this.datasetEntity == null){
                System.out.println("Path to dataset (arff or csv): ");
                this.datasetEntity = new DatasetEntity(scanner.nextLine()); 
                this.workingEntity = this.datasetEntity;
                System.out.println(this.datasetEntity.getDataset().toSummaryString());
                System.out.println(this.datasetEntity.getDataset().enumerateAttributes());
            }
            System.out.println("Enter your command: ");
            currentCommand = scanner.nextLine(); 
            command(currentCommand);
        }
    }

    private void commandMapper(String command1, String command2, String options) {
        try{
            switch(command1.toLowerCase()){
                case "stop" -> stop();
                case "classifier" -> callClassifier(command2, options);
                case "processing" -> callProcessing(command2, options);
                case "reset" -> callReset();
                case "index" -> callSetClassIndex(command2);
                case "predict" -> callTesting(command2);
                case "change_testing" -> callChangeTesting(command2);
                case "change_dataset" -> callChangePath(command2);
                default -> System.out.println("Invalid command");
            }
        } catch (Exception e){
            System.out.println("Command error: check for datatype, class index, etc..." + e.getMessage());
        }
    }

    private List<String> commandProcessing (String command){
        List<String> map = new ArrayList<>();
        int i = 0;
        int mem = 0;
        while(i <= command.length()){
            if(i == command.length() || map.size() == 2){
                map.add(command.substring(mem));
                break;
            }

            if(command.charAt(i) == ' '){
                map.add(command.substring(mem, i));
                mem = i + 1;
            }
            
            i++;
        }
        return map;
    }

    private void command(String command) throws Exception{
        List<String> list = commandProcessing(command);
        switch(list.size()){
            case 1 -> {
                commandMapper(list.get(0), "", "");
            }

            case 2 -> {
                commandMapper(list.get(0), list.get(1), "");
            }

            default -> {
                commandMapper(list.get(0), list.get(1), list.get(2));
            }
        }
    }

    private void callSetDataset(String path){
        try {
            this.datasetEntity = new DatasetEntity(path);
            this.workingEntity = this.datasetEntity;
        } catch (Exception e) {
            System.err.println("Invalid input dataset:"+e.getMessage());
        }
    }

    private void callProcessing(String processType, String options) throws Exception{
        workingEntity.setDataset(dataProcessingService.processingMapping(processType, workingEntity.getDataset(), Utils.splitOptions(options)));
        System.out.println(workingEntity.getDataset().toSummaryString());
    }

    private void callClassifier(String processType, String options) throws Exception{
        workingClassifier = dataClassificationService.classifierMapper(processType, workingEntity.getDataset(), Utils.splitOptions(options));
        System.out.println(workingClassifier);
    }

    private void callReset(){
        this.workingEntity = this.datasetEntity;
        System.out.println("Working data has been reset");
    }

    private void callChangeTesting(final String path) throws Exception {
        DatasetEntity newTest = new DatasetEntity(path);
        if (newTest.getDataset() == null) {
            System.out.println("Failed to load testing dataset from: " + path);
            return;
        }
        newTest.getDataset().setClassIndex(this.workingEntity.getDataset().classIndex());
        this.testingEntity = newTest;
        System.out.println("Testing dataset has been updated:");
        System.out.println(this.testingEntity.getDataset().toSummaryString());
    }

    private void callChangePath(final String path) throws Exception {
        DatasetEntity newData = new DatasetEntity(path);
        if (newData.getDataset() == null) {
            System.out.println("Failed to load dataset from: " + path);
            return;
        }
        this.datasetEntity = newData;
        this.workingEntity = newData;
        System.out.println("Main dataset has been changed:");
        System.out.println(this.datasetEntity.getDataset().toSummaryString());
    }

    private void callTesting(String options) throws Exception{
        if(testingEntity == null){
            System.out.println("Path to testing dataset (arff or csv): ");
        }
        System.out.println(dataTestingService.evaluation(workingEntity.getDataset(), testingEntity.getDataset(), workingClassifier, Utils.splitOptions(options)));
    }

    private void callSetClassIndex(String inputIndex) throws Exception{
        int index = Integer.parseInt(inputIndex);
        if(index > workingEntity.getDataset().numAttributes() || index < 0){
            System.out.println("Invalid index");
            return;
        }

        workingEntity.getDataset().setClassIndex(index);

        System.out.println("New class index:" + workingEntity.getDataset().classIndex());
    }

    private void stop(){
        this.scanner.close();
        this.isRunning = false;
    }
}
