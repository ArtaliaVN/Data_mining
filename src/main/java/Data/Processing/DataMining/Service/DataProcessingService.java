package Data.Processing.DataMining.Service;
import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.supervised.instance.SpreadSubsample;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.NumericCleaner;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RemoveUseless;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.instance.NonSparseToSparse;
import weka.filters.unsupervised.instance.RemoveDuplicates;
import weka.filters.unsupervised.instance.RemoveWithValues;
import weka.filters.unsupervised.instance.Resample;

public class DataProcessingService {

    public DataProcessingService(){}

    public Instances processingMapping(String command, Instances dataset, String[] inputOptions) throws Exception{
        return switch(command.toLowerCase()){
            case "remove_duplicates" -> removeDuplicates(dataset, inputOptions);
            case "standardize" -> standardize(dataset, inputOptions);
            case "discretizing" -> discretizing(dataset, inputOptions);
            case "replace_missing_value" -> replaceMissingValue(dataset, inputOptions);
            case "remove_missing_value" -> removeMissingValue(dataset, inputOptions);
            case "remove_useless" -> removeUseless(dataset, inputOptions);
            case "normalize" -> normalize(dataset, inputOptions);
            case "numeric_to_nomial" -> numericToNomial(dataset, inputOptions);
            case "string_to_nomial" -> stringToNomial(dataset, inputOptions);
            case "remove_attributes" -> removeAttributes(dataset, inputOptions);
            case "sparse_data" -> sparseData(dataset);
            case "unsupervised_resample" -> resample(dataset, inputOptions);
            case "supervised" -> resampleSupervised(dataset, inputOptions);
            case "spread_supsample" -> spreadSubsample(dataset, inputOptions);
            default -> dataset;
        };
    }
   
    public Instances processing(Instances dataset, String[] inputOptions) throws Exception{
        System.out.println("Processing dataset...");
        if(checkForDuplicates(dataset)>0){
            dataset = removeDuplicates(dataset, inputOptions);
            System.out.println(dataset.numInstances());
        } else {
            System.out.println("No duplicates found.");
        }
        //sparse dataset
        dataset = sparseData(dataset);
        //discretizing dataset
        dataset = discretizing(dataset, null);
        //remove useless attributes
        dataset = removeUseless(dataset, null);
        //remove missing values
        dataset = removeMissingValue(dataset, null);
        //replace missing values
        dataset = replaceMissingValue(dataset, null);
        //dataset after preprocessing
        System.out.println("Dataset after preprocessing:");
        System.out.println("Number of instances: " + dataset.numInstances());
        System.out.println("Number of attributes: " + dataset.numAttributes());
        //save dataset to file
        String filePath = "src/main/java/Data/Datasets/preprocessing_dataset.arff";
        saveDataset(filePath, dataset);
        return dataset;
    }

    //save dataset to file
    public void saveDataset(String filePath, Instances dataset) throws Exception {
        System.out.println("Saving dataset to " + filePath);
        ArffSaver saver = new ArffSaver();
        saver.setInstances(dataset);
        saver.setFile(new File(filePath));
        saver.writeBatch();
        System.out.println("Dataset saved successfully.");
        
    }

    public Instances removeDuplicates(Instances dataset, String[] inputOptions) throws Exception{
        System.out.println("Removing duplicates...");

        RemoveDuplicates removeDuplicates = new RemoveDuplicates();
        
        removeDuplicates.setInputFormat(dataset);
        Instances noDuplicates = Filter.useFilter(dataset, removeDuplicates);
        if(inputOptions != null)
            removeDuplicates.setOptions(inputOptions);
        int originalCount = dataset.numInstances();
        int deduplicatedCount = noDuplicates.numInstances();
        System.out.println("Original count: " + originalCount);
        System.out.println("Deduplicated count: " + deduplicatedCount);
        System.out.println("Duplicates removed: " + (originalCount - deduplicatedCount));
        return noDuplicates;
    }

    //check for duplicates in the dataset
    public int checkForDuplicates(Instances dataset) throws Exception{
        System.out.println("Checking for duplicates...");
        if (dataset.classIndex() != -1 && dataset.classAttribute().numValues() <= 1) {
            dataset.setClassIndex(-1);  // Weka filter can't handle unary class
        }
        RemoveDuplicates removeDuplicates = new RemoveDuplicates();
        removeDuplicates.setInputFormat(dataset);
        Instances noDuplicates = Filter.useFilter(dataset, removeDuplicates);

        //compare count of instances
        int originalCount = dataset.numInstances();
        int deduplicatedCount = noDuplicates.numInstances();

        return originalCount - deduplicatedCount;
    }

    //This will return new dataset without input attributes array, the old dataset will not be replaced.
    //The input is taken as an array of string
    public Instances removeAttributes(Instances dataset, String[] inputOptions) throws Exception{
        Remove remove = new Remove();
        if(inputOptions != null)
            remove.setOptions(inputOptions);
        remove.setInputFormat(dataset);
        return Filter.useFilter(dataset, remove);
    }

    //For working with dataset type with multiple repetitive '0' attribute, this function reduces the dataset
    //in shorter manner
    //Example:
    //{1,4,0,0,0,2,78,0,0,0,0} we have 7 '0' attributes which will expand the dataset if there are millions of those instances
    //{0 1, 1 4, 5 2, 6 78} the dataset becomes shorter when very index there are 2 number represent the current index and 
    //current value.
    //This will make the dataset more efficient if there are too many '0' valued-attribute in the dataset 
    public Instances sparseData(Instances dataset) throws Exception{
        NonSparseToSparse sparseFilter = new NonSparseToSparse();
        sparseFilter.setInputFormat(dataset);
        return Filter.useFilter(dataset, sparseFilter);
    }

    //Use for reducing noise, smoothing dataset
    public Instances discretizing(Instances dataset, String[] inputOptions) throws Exception{
        Discretize discretize = new Discretize();
        if(inputOptions != null)
            discretize.setOptions(inputOptions);
        discretize.setInputFormat(dataset);
        return Filter.useFilter(dataset, discretize);
    }

    public Instances numericCleaner(Instances dataset, String[] inputOptions) throws Exception {
        NumericCleaner numericCleaner = new NumericCleaner();
        if(inputOptions != null)
            numericCleaner.setOptions(inputOptions);
        numericCleaner.setInputFormat(dataset);
        return Filter.useFilter(dataset, numericCleaner);
    }

    public Instances replaceMissingValue(Instances dataset, String[] inputOptions) throws Exception {
        ReplaceMissingValues replace = new ReplaceMissingValues();
        if(inputOptions != null)
            replace.setOptions(inputOptions);
        replace.setInputFormat(dataset);
        return Filter.useFilter(dataset, replace);
    }

    public Instances removeMissingValue(Instances dataset, String[] inputOptions) throws Exception {
        RemoveWithValues remove = new RemoveWithValues();
        if(inputOptions != null)
            remove.setOptions(inputOptions);
        remove.setInputFormat(dataset);
        return Filter.useFilter(dataset, remove);
    }

    //Remove attributes with little to no impact to dataset
    public Instances removeUseless(Instances dataset, String[] inputOptions) throws Exception {
        RemoveUseless remove = new RemoveUseless();
        if(inputOptions != null)
            remove.setOptions(inputOptions);
        remove.setInputFormat(dataset);
        return Filter.useFilter(dataset, remove);
    }

    //Turn String data in dataset into Nomial data that weka could read
    public Instances stringToNomial(Instances dataset, String[] inputOptions) throws Exception {
        StringToNominal filter = new StringToNominal();
        if(inputOptions != null)
            filter.setOptions(inputOptions);
        filter.setAttributeRange("first-last"); 
        filter.setInputFormat(dataset);
        return Filter.useFilter(dataset, filter);
    }

    //Rescales numeric values to [0,1]
    public Instances normalize(Instances dataset, String[] inputOptions) throws Exception {
        Normalize normalize = new Normalize();
        if(inputOptions != null)
            normalize.setOptions(inputOptions);
        normalize.setInputFormat(dataset);
        return Filter.useFilter(dataset, normalize);
    }

    //Standardizes values (zero mean, unit variance)
    public Instances standardize(Instances dataset, String[] inputOptions) throws Exception {
        Standardize standardize = new Standardize();
        if(inputOptions != null)
            standardize.setOptions(inputOptions);
        standardize.setInputFormat(dataset);
        return Filter.useFilter(dataset, standardize);
    }

    public Instances removeDuplication(Instances dataset, String[] inputOptions) throws Exception{
        RemoveDuplicates removeDuplicates = new RemoveDuplicates();
        if(inputOptions != null)
            removeDuplicates.setOptions(inputOptions);
        removeDuplicates.setInputFormat(dataset);
        return Filter.useFilter(dataset, removeDuplicates);
    }

    public Instances numericToNomial(Instances dataset, String[] inputOptions) throws Exception{
        NumericToNominal numericToNominal = new NumericToNominal();
        if(inputOptions != null)
            numericToNominal.setOptions(inputOptions);
        numericToNominal.setInputFormat(dataset);
        return Filter.useFilter(dataset, numericToNominal);
    }

    public Instances remove(Instances dataset, String[] inputOptions) throws Exception{
        Remove remove = new Remove();
        if(inputOptions != null)
            remove.setOptions(inputOptions);
        remove.setInputFormat(dataset);
        return Filter.useFilter(dataset, remove);
    }

    public Instances resample(Instances dataset, String[] inputOptions) throws Exception {
        Resample resample = new Resample();
        if(inputOptions != null)
            resample.setOptions(inputOptions);
        return Filter.useFilter(dataset, resample);
    }

    public Instances resampleSupervised(Instances dataset, String[] inputOptions) throws Exception {
        weka.filters.supervised.instance.Resample resample = new weka.filters.supervised.instance.Resample();
        if(inputOptions != null)
            resample.setOptions(inputOptions);
        return Filter.useFilter(dataset, resample);
    }

    public Instances spreadSubsample(Instances dataset, String[] inputOptions) throws Exception {
        SpreadSubsample spreadSubsample = new SpreadSubsample();
        if(inputOptions != null)
            spreadSubsample.setOptions(inputOptions);
        return Filter.useFilter(dataset, spreadSubsample);
    }
}
