package Data.Processing.DataMining.Entity;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import org.apache.commons.io.FilenameUtils;

import weka.core.Instances;
import weka.core.converters.CSVLoader;
import weka.core.converters.ConverterUtils.DataSource;

public class DatasetEntity {

    private Instances dataset;
    
    public DatasetEntity(byte[] file) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(file); 
        DataSource dataSource = new DataSource(inputStream);
        this.dataset = dataSource.getDataSet();
    }

    public DatasetEntity(String filePath) throws Exception {
        switch(FilenameUtils.getExtension(filePath)){
            case "arff" -> {
                DataSource dataSource = new DataSource(filePath);
                this.dataset = dataSource.getDataSet();
            }
            
            case "csv" -> {
                CSVLoader loader = new CSVLoader();
                loader.setSource(new File(filePath));
                this.dataset = loader.getDataSet();
            }
        }
    }

    public void setDataset(Instances dataset){
        this.dataset = dataset;
        this.dataset.setClassIndex(dataset.classIndex());
    }

    public void setDataset(byte[] file) throws Exception{
        InputStream inputStream = new ByteArrayInputStream(file); 
        DataSource dataSource = new DataSource(inputStream);
        this.dataset = dataSource.getDataSet();
    }

    public Instances getDataset(){
        return dataset;
    }
    
}
