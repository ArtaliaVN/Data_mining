package Data.Processing.DataMining.Service.utils;

import java.io.File;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Normalize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.RemoveUseless;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.attribute.Standardize;
import weka.filters.unsupervised.attribute.StringToNominal;
import weka.filters.unsupervised.instance.NonSparseToSparse;
import weka.filters.unsupervised.instance.RemoveDuplicates;
import weka.filters.unsupervised.instance.RemoveWithValues;

public class PreprocessingHandler {
    Instances dataset;
    //constructor
    public PreprocessingHandler(Instances dataset) throws Exception {
        this.dataset = dataset;
    }
    
    //save dataset to file
    public void saveDataset(String filePath) throws Exception {
        System.out.println("Saving dataset to " + filePath);
        ArffSaver saver = new ArffSaver();
        try{
            saver.setInstances(dataset);
            saver.setFile(new File(filePath));
            saver.writeBatch();
            System.out.println("Dataset saved successfully.");
        }catch (Exception e){
            System.out.println("Error: " + e.getMessage());
        }
    }

    public Instances removeDuplicates(Instances dataset) throws Exception{
        System.out.println("Removing duplicates...");
        if (dataset.classIndex() != -1 && dataset.classAttribute().numValues() <= 1) {
            dataset.setClassIndex(-1);  // Weka filter can't handle unary class
        }

        RemoveDuplicates removeDuplicates = new RemoveDuplicates();
        removeDuplicates.setInputFormat(dataset);
        Instances noDuplicates = Filter.useFilter(dataset, removeDuplicates);

        int originalCount = dataset.numInstances();
        int deduplicatedCount = noDuplicates.numInstances();
        System.out.println("Removed duplicates finished. " + (originalCount - deduplicatedCount) + " duplicates removed.");
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
        Instances newDataset = Filter.useFilter(dataset, remove);
        return newDataset;
    }

    //For working with dataset type with multiple repetive '0' attribute, this function reduces the dataset
    //in shorter manner
    //Example:
    //{1,4,0,0,0,2,78,0,0,0,0} we have 7 '0' attributes which will expand the dataset if there are millions of those instances
    //{0 1, 1 4, 5 2, 6 78} the dataset becomes shorter when very index there are 2 number represent the current index and 
    //current value.
    //This will make the dataset more efficient if there are too many '0' valued-attribute in the dataset 
    public Instances sparseData(Instances dataset) throws Exception{
        NonSparseToSparse sparseFilter = new NonSparseToSparse();
        sparseFilter.setInputFormat(dataset);
        Instances newDataset = Filter.useFilter(dataset, sparseFilter);
        return newDataset;
    }

    //Use for reducing noise, smoothing dataset
    public Instances discretizing(Instances dataset, String[] inputOptions) throws Exception{
        Discretize discretize = new Discretize();
        if(inputOptions != null)
            discretize.setOptions(inputOptions);
        discretize.setInputFormat(dataset);
        Instances newDataset = Filter.useFilter(dataset, discretize);
        return newDataset;
    }

    public Instances replaceMissingValue(Instances dataset) throws Exception {
        System.out.println("Replacing missing values...");
        ReplaceMissingValues replace = new ReplaceMissingValues();
        replace.setInputFormat(dataset);

        Instances newDataset = Filter.useFilter(dataset, replace);
        PreprocessingHandler handler = new PreprocessingHandler(newDataset);
        //check for missing values again
        if(handler.checkForMissingValues() > 0){
            System.out.println("Missing values failed.");
        } else{
            System.out.println("Missing values replaced successfully.");
        }
        return newDataset;
    }

    public Instances removeMissingValue(Instances dataset, String[] inputOptions) throws Exception {
        RemoveWithValues remove = new RemoveWithValues();
        if(inputOptions != null)
            remove.setOptions(inputOptions);
        remove.setInputFormat(dataset);
        Instances newDataset = Filter.useFilter(dataset, remove);
        return newDataset;
    }

    //Remove attributes with little to no impact to dataset
    public Instances removeUseless(Instances dataset, String[] inputOptions) throws Exception {
        RemoveUseless remove = new RemoveUseless();
        if(inputOptions != null)
            remove.setOptions(inputOptions);
        remove.setInputFormat(dataset);
        Instances newDataset = Filter.useFilter(dataset, remove);
        return newDataset;
    }

    //Turn String data in dataset into Nomial data that weka could read
    public Instances stringToNomial(Instances dataset, String[] inputOptions) throws Exception {
        StringToNominal filter = new StringToNominal();
        if(inputOptions != null)
            filter.setOptions(inputOptions);
        filter.setAttributeRange("first-last"); 
        filter.setInputFormat(dataset);
        Instances newDataset = Filter.useFilter(dataset, filter);
        return newDataset;
    }

    //Rescales numeric values to [0,1]
    public Instances Normalize(Instances dataset, String[] inputOptions) throws Exception {
        Normalize normalize = new Normalize();
        if(inputOptions != null)
            normalize.setOptions(inputOptions);
        normalize.setInputFormat(dataset);
        Instances newDataset = Filter.useFilter(dataset, normalize);
        return newDataset;
    }

    //Standardizes values (zero mean, unit variance)
    public Instances Standardize(Instances dataset, String[] inputOptions) throws Exception {
        Standardize standardize = new Standardize();
        if(inputOptions != null)
            standardize.setOptions(inputOptions);
        standardize.setInputFormat(dataset);
        Instances newDataset = Filter.useFilter(dataset, standardize);
        return newDataset;
    }

    //check for missing values in the dataset
    public int checkForMissingValues() throws Exception{
        int missingCount = 0;
        Instances missInstances = new Instances(dataset,0);// create empty instances


        for (int i = 0; i < dataset.numInstances(); i++) {
            boolean hasMissing = false;
            for (int j = 0; j < dataset.numAttributes(); j++) {
                if (dataset.instance(i).isMissing(j)) {
                    missingCount++;
                    hasMissing = true;
                }
            }
            if (hasMissing) {
                missInstances.add(dataset.instance(i));
            }
        }

        if (missingCount > 0) {
            System.out.println("⚠️ Found " + missingCount + " missing values in the dataset.");
            System.out.println("💡 Extracted " + missInstances.numInstances() + " instances that contain missing values.");
            PreprocessingHandler handler = new PreprocessingHandler(missInstances);
            handler.saveDataset("src/main/java/Data/Datasets/missing_values_dataset.arff");
            //fill the missing values of missInstances dataset
            Instances filledInstances = handler.replaceMissingValue(missInstances);
            PreprocessingHandler filledHandler = new PreprocessingHandler(filledInstances);
            filledHandler.saveDataset("src/main/java/Data/Datasets/fill_missing_values_dataset.arff");
            System.out.println("✅ Missing values replaced successfully.");
            return missingCount;
        } else {
            System.out.println("✅ No missing values found in the dataset.");
            return 0;
        }
    }
    
}