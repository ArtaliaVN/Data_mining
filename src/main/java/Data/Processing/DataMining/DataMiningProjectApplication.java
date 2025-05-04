package Data.Processing.DataMining;
import Data.Processing.DataMining.Service.CommandLineService;

public class DataMiningProjectApplication {

	public static void main(String[] args) throws Exception {
		CommandLineService cmdLineService = new CommandLineService();
		cmdLineService.cmdRunning();
	}
}
