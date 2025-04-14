package Data.Processing.DataMining.Service;
import org.springframework.stereotype.Service;

import Data.Processing.DataMining.Entity.DatasetEntity;
import lombok.Data;
import lombok.NoArgsConstructor;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Discretize;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.ReplaceMissingValues;
import weka.filters.unsupervised.instance.NonSparseToSparse;
import weka.filters.unsupervised.instance.RemoveWithValues;

@Service
@Data
@NoArgsConstructor
public class DataProcessingService {

    //This will return new dataset without input attributes array, the old dataset will not be replaced.
    //The input is taken as an array of string
    public Instances removeAttributes(DatasetEntity dataset, String[] inputOptions) throws Exception{
        Remove remove = new Remove();
        remove.setOptions(inputOptions);
        remove.setInputFormat(dataset.getDataset());
        Instances newDataset = Filter.useFilter(dataset.getDataset(), remove);
        return newDataset;
    }

    //This will return new dataset without input attributes array, the old dataset will not be replaced.
    //The input is taken as a string
    public Instances removeAttributes(DatasetEntity dataset, String inputOptions) throws Exception{
        Remove remove = new Remove();
        remove.setOptions(weka.core.Utils.splitOptions(inputOptions));
        remove.setInputFormat(dataset.getDataset());
        Instances newDataset = Filter.useFilter(dataset.getDataset(), remove);
        return newDataset;
    }

    //For working with dataset type with multiple repetive '0' attribute, this function reduces the dataset
    //in shorter manner
    //Example:
    //{1,4,0,0,0,2,78,0,0,0,0} we have 7 '0' attributes which will expand the dataset if there are millions of those instances
    //{0 1, 1 4, 5 2, 6 78} the dataset becomes shorter when very index there are 2 number represent the current index and 
    //current value.
    //This will make the dataset more efficient if there are too many '0' valued-attribute in the dataset 
    public Instances sparseData(DatasetEntity dataset) throws Exception{
        NonSparseToSparse sparseFilter = new NonSparseToSparse();
        sparseFilter.setInputFormat(dataset.getDataset());
        Instances newDataset = Filter.useFilter(dataset.getDataset(), sparseFilter);
        return newDataset;
    }

    //Use for reducing noise, smoothing dataset
    public Instances discretizing(DatasetEntity dataset, String[] inputOptions) throws Exception{
        Discretize discretize = new Discretize();
        discretize.setOptions(inputOptions);
        discretize.setInputFormat(dataset.getDataset());
        Instances newDataset = Filter.useFilter(dataset.getDataset(), discretize);
        return newDataset;
    }

    public Instances discretizing(DatasetEntity dataset, String inputOptions) throws Exception{
        Discretize discretize = new Discretize();
        discretize.setOptions(weka.core.Utils.splitOptions(inputOptions));
        discretize.setInputFormat(dataset.getDataset());
        Instances newDataset = Filter.useFilter(dataset.getDataset(), discretize);
        return newDataset;
    }

    public Instances replaceMissingValue(DatasetEntity dataset) throws Exception {
        ReplaceMissingValues replace = new ReplaceMissingValues();
        replace.setInputFormat(dataset.getDataset());
        Instances newDataset = Filter.useFilter(dataset.getDataset(), replace);
        return newDataset;
    }

    public Instances removeMissingValue(DatasetEntity dataset) throws Exception {
        RemoveWithValues remove = new RemoveWithValues();
        remove.setInputFormat(dataset.getDataset());
        Instances newDataset = Filter.useFilter(dataset.getDataset(), remove);
        return newDataset;
    }

}
