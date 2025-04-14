package Data.Processing.DataMining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Data.Processing.DataMining.Entity.DatasetEntity;
import Data.Processing.DataMining.Service.DataClassificationService;
import Data.Processing.DataMining.Service.DataProcessingService;

@SpringBootApplication
public class DataMiningProjectApplication {

	public static void main(String[] args) throws Exception {
		DatasetEntity trainingEntity = new DatasetEntity("src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\iris.arff");
		DataClassificationService classificationService = new DataClassificationService();
		DataProcessingService dataProcessingService = new DataProcessingService();
		System.out.println(dataProcessingService.replaceMissingValue(trainingEntity));
		System.out.println(classificationService.J48(trainingEntity, null));
		SpringApplication.run(DataMiningProjectApplication.class, args);
	}
}
