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
        if(list.size() == 1){
            commandMapper(list.get(0), "", "");
        }
        if(list.size() == 2){
            commandMapper(list.get(0), list.get(1), "");
        }
        if(list.size() >= 3){
            commandMapper(list.get(0), list.get(1), list.get(2));
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

    private void callTesting(String path) throws Exception{
        DatasetEntity testingEntity = new DatasetEntity(path);
        testingEntity.getDataset().setClassIndex(workingEntity.getDataset().classIndex());
        if(workingClassifier == null){
            System.err.println("This action requires classification");
            return;
        }

        System.out.println(dataTestingService.evaluation(workingEntity.getDataset(), testingEntity.getDataset(), workingClassifier));
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
