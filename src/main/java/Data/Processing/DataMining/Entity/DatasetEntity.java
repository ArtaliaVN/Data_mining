package Data.Processing.DataMining.Entity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import lombok.Data;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

@Data
public class DatasetEntity {
    private Instances dataset;

    private int instancesNumber;

    private int attributesNumber;
    
    public DatasetEntity(byte[] file) throws Exception {
        InputStream inputStream = new ByteArrayInputStream(file); 
        DataSource dataSource = new DataSource(inputStream);
        this.dataset = dataSource.getDataSet();
        this.instancesNumber = dataset.numInstances();
        this.attributesNumber = dataset.numAttributes();
    }

    public void setDataset(Instances dataset){
        this.dataset = dataset;
        this.instancesNumber = this.dataset.numInstances();
        this.attributesNumber = this.dataset.numAttributes();
    }

    public void setDataset(byte[] file) throws Exception{
        InputStream inputStream = new ByteArrayInputStream(file); 
        DataSource dataSource = new DataSource(inputStream);
        this.dataset = dataSource.getDataSet();
        this.instancesNumber = dataset.numInstances();
        this.attributesNumber = dataset.numAttributes();
    }
}
