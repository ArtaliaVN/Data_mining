package Data.Processing.DataMining.Service;

import org.springframework.stereotype.Service;

import Data.Processing.DataMining.Service.utils.PreprocessingHandler;
import lombok.Data;
import lombok.NoArgsConstructor;
import weka.core.Instances;

@Service
@Data
@NoArgsConstructor
public class DataProcessingService {

    //handle the dataset without using main function
    public DataProcessingService(Instances dataset) throws Exception {
        System.out.println("Processing dataset...");
        PreprocessingHandler pHandler = new PreprocessingHandler(dataset);
        //check for duplicates
        if(pHandler.checkForDuplicates(dataset) > 0){
            System.out.println("Duplicates found in the dataset.");
            dataset = pHandler.removeDuplicates(dataset);
            //save the dataset
            pHandler.saveDataset("src/main/java/Data/Datasets/processed_dataset.arff");
        }

        //check for missing values
        pHandler.checkForMissingValues();


    }
}