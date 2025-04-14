package Data.Processing.DataMining;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Data.Processing.DataMining.Entity.DatasetEntity;
import Data.Processing.DataMining.Service.DataClassificationService;
import Data.Processing.DataMining.Service.DataProcessingService;
import Data.Processing.DataMining.Service.DataTestingService;

@SpringBootApplication
public class DataMiningProjectApplication {

	public static void main(String[] args) throws Exception {
		DatasetEntity trainingEntity = new DatasetEntity("src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\ReutersGrain-train.arff");
		DatasetEntity testingEntity = new DatasetEntity("src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\ReutersGrain-test.arff");

		DataClassificationService classificationService = new DataClassificationService();
		DataProcessingService dataProcessingService = new DataProcessingService();
		DataTestingService dataTestingService = new DataTestingService();
		trainingEntity.setDataset(dataProcessingService.stringToNomial(trainingEntity.getDataset(), null));
		System.out.println(dataTestingService.evaluation(dataProcessingService.stringToNomial(trainingEntity.getDataset(), null), dataProcessingService.stringToNomial(testingEntity.getDataset(), null), classificationService.J48(dataProcessingService.stringToNomial(testingEntity.getDataset(), null), null)).toSummaryString());
		SpringApplication.run(DataMiningProjectApplication.class, args);
	}
}
