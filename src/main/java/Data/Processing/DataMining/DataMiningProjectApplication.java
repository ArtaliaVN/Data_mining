package Data.Processing.DataMining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Data.Processing.DataMining.Entity.DatasetEntity;
import Data.Processing.DataMining.Service.DataClassificationService;
import Data.Processing.DataMining.Service.DataProcessingService;
import Data.Processing.DataMining.Service.DataTestingService;
import weka.core.Instances;
import weka.knowledgeflow.Data;

@SpringBootApplication
public class DataMiningProjectApplication {

	public static void main(String[] args) throws Exception {
		DatasetEntity dataset1 = new DatasetEntity("src/main/java/Data/Datasets/summer-products-with-rating-and-performance_2020-08.csv");

		DataProcessingService dataProcessingService = new DataProcessingService(dataset1.getDataset());


	}
}
