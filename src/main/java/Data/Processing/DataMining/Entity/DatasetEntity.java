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
        this.dataset.setClassIndex(this.dataset.numAttributes() - 1);
    }

    public DatasetEntity(String filePath) throws Exception {
        switch(FilenameUtils.getExtension(filePath)){
            case "arff" -> {
                DataSource dataSource = new DataSource(filePath);
                this.dataset = dataSource.getDataSet();
                this.dataset.setClassIndex(this.dataset.numAttributes() - 1);
            }
            
            case "csv" -> {
                CSVLoader loader = new CSVLoader();
                loader.setSource(new File(filePath));
                this.dataset = loader.getDataSet();
                this.dataset.setClassIndex(this.dataset.numAttributes() - 1);
            }
        }
    }

    public void setDataset(Instances dataset){
        this.dataset = dataset;
        this.dataset.setClassIndex(this.dataset.numAttributes() - 1);
    }

    public void setDataset(byte[] file) throws Exception{
        InputStream inputStream = new ByteArrayInputStream(file); 
        DataSource dataSource = new DataSource(inputStream);
        this.dataset = dataSource.getDataSet();
        if (this.dataset.classIndex() != -1 && this.dataset.classAttribute().numValues() <= 1) {
            this.dataset.setClassIndex(-1);  // Weka filter can't handle unary class
        }
    }

    public Instances getDataset(){
        return dataset;
    }
    
}
