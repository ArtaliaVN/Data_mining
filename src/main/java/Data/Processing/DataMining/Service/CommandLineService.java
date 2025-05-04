package Data.Processing.DataMining.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import Data.Processing.DataMining.Entity.DatasetEntity;
import weka.core.Utils;

public class CommandLineService {
    private Scanner scanner = new Scanner(System.in);
    private boolean isRunning = true;
    private String currentCommand = "";
    private DatasetEntity datasetEntity;

    private DataProcessingService dataProcessingService;

    private DataClassificationService dataClassificationService;

    private DataTestingService dataTestingService;

    public void cmdRunning() throws Exception{
        this.dataClassificationService = new DataClassificationService();
        this.dataProcessingService = new DataProcessingService();
        this.dataTestingService = new DataTestingService();
        while(isRunning){
            if(this.datasetEntity == null){
                System.out.println("Path to dataset (arff or csv): ");
                this.datasetEntity = new DatasetEntity(scanner.nextLine()); 
                System.out.println(this.datasetEntity.getDataset().toSummaryString());
            }
            System.out.println("Enter your command: ");
            currentCommand = scanner.nextLine(); 
            command(currentCommand);
        }
    }

    private void commandMapper(String command1, String command2, String options) throws Exception{
        switch(command1){
            case "stop" -> stop();
            case "classifier" -> callClassifier(command2, options);
            case "processing" -> callProcessing(command2, options);
            default -> System.out.println("Invalid command");
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
            commandMapper(list.get(0), list.get(1), null);
        }
        if(list.size() >= 3){
            commandMapper(list.get(0), list.get(1), list.get(2));
        }
    }

    private void callProcessing(String processType, String options) throws Exception{
        datasetEntity.setDataset(dataProcessingService.processingMapping(processType, datasetEntity.getDataset(), Utils.splitOptions(options)));
        System.out.println(datasetEntity.getDataset().toSummaryString());
    }

    private void callClassifier(String processType, String options) throws Exception{
        System.out.println(dataClassificationService.classifierMapper(processType, datasetEntity.getDataset(), Utils.splitOptions(options)));
    }

    private void callTesting(String command, String path) throws Exception{
        DatasetEntity testingEntity = new DatasetEntity(path);
        int lB = 0;
        int hB = 0;
        if(command.length() == 0)
            return;
        for(int i = 0; i < command.length(); i++){
            if(command.charAt(i) == ' ')
                hB = i - 1;
        }
    }

    private void stop(){
        this.scanner.close();
        this.isRunning = false;
    }
}
