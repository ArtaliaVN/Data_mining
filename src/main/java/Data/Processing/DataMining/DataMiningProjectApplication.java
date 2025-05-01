package Data.Processing.DataMining;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import Data.Processing.DataMining.Entity.DatasetEntity;
import Data.Processing.DataMining.Service.DataClassificationService;
import Data.Processing.DataMining.Service.DataProcessingService;
import Data.Processing.DataMining.Service.DataTestingService;
import weka.core.Utils;
import java.util.Scanner;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.classifiers.Classifier;

@SpringBootApplication
public class DataMiningProjectApplication {

	public static void main(String[] args) throws Exception {
		DatasetEntity trainingEntity = new DatasetEntity("src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\ReutersGrain-train.arff");
		DatasetEntity testingEntity = new DatasetEntity("src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\ReutersGrain-test.arff");

		DataProcessingService dataProcessingService = new DataProcessingService();
		DataClassificationService classificationService = new DataClassificationService();
		
		DataTestingService dataTestingService = new DataTestingService();
		//System.out.println(dataProcessingService.Normalize(trainingEntity.getDataset(), null).toSummaryString());
		trainingEntity.setDataset(dataProcessingService.stringToNomial(trainingEntity.getDataset(), null));
		System.out.println(dataTestingService.evaluation(dataProcessingService.stringToNomial(trainingEntity.getDataset(), null), dataProcessingService.stringToNomial(testingEntity.getDataset(), null), classificationService.BayesNet(dataProcessingService.stringToNomial(testingEntity.getDataset(), null), null)).toSummaryString());
		SpringApplication.run(DataMiningProjectApplication.class, args);
		try {
			Scanner scanner = new Scanner(System.in);
			DataProcessingService processingService = new DataProcessingService();
			DataClassificationService classificationService = new DataClassificationService();

			// Load your dataset first
			System.out.print("Enter dataset path (.arff file): ");
			String datasetPath = scanner.nextLine();
			DataSource source = new DataSource(datasetPath);
			Instances dataset = source.getDataSet();

			if (dataset.classIndex() == -1) {
				dataset.setClassIndex(dataset.numAttributes() - 1); // Set last attribute as class if not set
			}

			boolean exit = false;

			while (!exit) {
				System.out.println("\n=== MAIN MENU ===");
				System.out.println("1. Processing Data");
				System.out.println("2. Classification");
				System.out.println("0. Exit");
				System.out.print("Your choice: ");
				int mainChoice = scanner.nextInt();
				scanner.nextLine(); // Consume newline

				switch (mainChoice) {
					case 1:
						boolean backToMain = false;
						while (!backToMain) {
							System.out.println("\n=== PROCESSING DATA MENU ===");
							System.out.println("1. Remove Attributes");
							System.out.println("2. Sparse Data");
							System.out.println("3. Discretize");
							System.out.println("4. Replace Missing Values");
							System.out.println("5. Remove Missing Values");
							System.out.println("6. Remove Useless Attributes");
							System.out.println("7. String to Nominal");
							System.out.println("8. Normalize");
							System.out.println("9. Standardize");
							System.out.println("0. Back to Main Menu");
							System.out.print("Your choice: ");

							int processingChoice = scanner.nextInt();
							scanner.nextLine(); // Consume newline

							switch (processingChoice) {
								case 1:
									System.out.println("Enter options for Remove (e.g., -R 1,2):");
									String removeOptions = scanner.nextLine();
									dataset = processingService.removeAttributes(dataset, Utils.splitOptions(removeOptions));
									break;
								case 2:
									dataset = processingService.sparseData(dataset);
									break;
								case 3:
									System.out.println("Enter options for Discretize (or leave empty):");
									String discretizeOptions = scanner.nextLine();
									dataset = processingService.discretizing(dataset,
											discretizeOptions.isEmpty() ? null : Utils.splitOptions(discretizeOptions));
									break;
								case 4:
									dataset = processingService.replaceMissingValue(dataset, null);
									break;
								case 5:
									System.out.println("Enter options for RemoveWithValues:");
									String removeMissingOptions = scanner.nextLine();
									dataset = processingService.removeMissingValue(dataset,
											removeMissingOptions.isEmpty() ? null : Utils.splitOptions(removeMissingOptions));
									break;
								case 6:
									dataset = processingService.removeUseless(dataset, null);
									break;
								case 7:
									dataset = processingService.stringToNomial(dataset, null);
									break;
								case 8:
									dataset = processingService.Normalize(dataset, null);
									break;
								case 9:
									dataset = processingService.Standardize(dataset, null);
									break;
								case 0:
									backToMain = true;
									break;
								default:
									System.out.println("Invalid choice. Try again.");
									break;
							}

							if (!backToMain && processingChoice >= 1 && processingChoice <= 9) {
								System.out.println("✅ Processing completed!");
								System.out.println("Current dataset: " + dataset.numInstances() + " instances, " + dataset.numAttributes() + " attributes.");
							}
						}
						break;

					case 2:
						boolean backToMainFromClassification = false;
						while (!backToMainFromClassification) {
							System.out.println("\n=== CLASSIFICATION MENU ===");
							System.out.println("1. J48");
							System.out.println("2. Naive Bayes");
							System.out.println("3. OneR");
							System.out.println("4. ZeroR");
							System.out.println("5. BayesNet");
							System.out.println("0. Back to Main Menu");
							System.out.print("Your choice: ");

							int classificationChoice = scanner.nextInt();
							scanner.nextLine(); // Consume newline

							Classifier classifier = null;

							switch (classificationChoice) {
								case 1:
									System.out.println("Enter options for J48 (or leave empty):");
									String j48Options = scanner.nextLine();
									classifier = classificationService.J48(dataset,
											j48Options.isEmpty() ? null : Utils.splitOptions(j48Options));
									break;
								case 2:
									System.out.println("Enter options for NaiveBayes (or leave empty):");
									String naiveOptions = scanner.nextLine();
									classifier = classificationService.NaiveBayes(dataset,
											naiveOptions.isEmpty() ? null : Utils.splitOptions(naiveOptions));
									break;
								case 3:
									System.out.println("Enter options for OneR (or leave empty):");
									String onerOptions = scanner.nextLine();
									classifier = classificationService.OneR(dataset,
											onerOptions.isEmpty() ? null : Utils.splitOptions(onerOptions));
									break;
								case 4:
									System.out.println("Enter options for ZeroR (or leave empty):");
									String zerorOptions = scanner.nextLine();
									classifier = classificationService.ZeroR(dataset,
											zerorOptions.isEmpty() ? null : Utils.splitOptions(zerorOptions));
									break;
								case 5:
									System.out.println("Enter options for BayesNet (or leave empty):");
									String bayesNetOptions = scanner.nextLine();
									classifier = classificationService.BayesNet(dataset,
											bayesNetOptions.isEmpty() ? null : Utils.splitOptions(bayesNetOptions));
									break;
								case 0:
									backToMainFromClassification = true;
									break;
								default:
									System.out.println("Invalid choice. Try again.");
									break;
							}

							if (classifier != null) {
								System.out.println("✅ Classifier built successfully: " + classifier.getClass().getSimpleName());
							}
						}
						break;

					case 0:
						exit = true;
						System.out.println("Exiting...");
						break;

					default:
						System.out.println("Invalid choice. Try again.");
						break;
				}
			}

			scanner.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
