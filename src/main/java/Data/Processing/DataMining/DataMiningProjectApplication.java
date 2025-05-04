package Data.Processing.DataMining;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import Data.Processing.DataMining.Entity.DatasetEntity;
import Data.Processing.DataMining.Service.CommandLineService;
import Data.Processing.DataMining.Service.DataProcessingService;

@SpringBootApplication
public class DataMiningProjectApplication {

	public static void main(String[] args) throws Exception {

		SpringApplication.run(DataMiningProjectApplication.class, args);
		CommandLineService cmdLineService = new CommandLineService();
		cmdLineService.cmdRunning();
	}
}
