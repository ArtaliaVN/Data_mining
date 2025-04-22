package Data.Processing.DataMining;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Data.Processing.DataMining.Entity.DatasetEntity;
import Data.Processing.DataMining.Service.DataClassificationService;
import Data.Processing.DataMining.Service.DataProcessingService;
<<<<<<< Updated upstream
import Data.Processing.DataMining.Service.DataTestingService;
=======
>>>>>>> Stashed changes

@SpringBootApplication
public class DataMiningProjectApplication {

	public static void main(String[] args) throws Exception {
<<<<<<< Updated upstream
		DatasetEntity trainingEntity = new DatasetEntity("src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\ReutersGrain-train.arff");
		DatasetEntity testingEntity = new DatasetEntity("src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\ReutersGrain-test.arff");

		DataProcessingService dataProcessingService = new DataProcessingService();
		DataClassificationService classificationService = new DataClassificationService();
		
		DataTestingService dataTestingService = new DataTestingService();
		//System.out.println(dataProcessingService.Normalize(trainingEntity.getDataset(), null).toSummaryString());
		trainingEntity.setDataset(dataProcessingService.stringToNomial(trainingEntity.getDataset(), null));
		System.out.println(dataTestingService.evaluation(dataProcessingService.stringToNomial(trainingEntity.getDataset(), null), dataProcessingService.stringToNomial(testingEntity.getDataset(), null), classificationService.BayesNet(dataProcessingService.stringToNomial(testingEntity.getDataset(), null), null)).toSummaryString());
=======
		DatasetEntity trainingEntity = new DatasetEntity("src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\cleaned_data.csv");
		DatasetEntity testingEntity = new DatasetEntity("src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\cleaned_insight_data.csv");

		DataProcessingService dataProcessingService = new DataProcessingService();
		DataClassificationService classificationService = new DataClassificationService();
		System.out.println(dataProcessingService.checkForDuplicates(trainingEntity.getDataset()));
	
>>>>>>> Stashed changes
		SpringApplication.run(DataMiningProjectApplication.class, args);
	}
}
