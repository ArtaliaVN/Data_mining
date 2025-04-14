package Data.Processing.DataMining.Service;

import org.springframework.stereotype.Service;
import weka.classifiers.trees.J48;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.NaiveBayes;
import Data.Processing.DataMining.Entity.DatasetEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@NoArgsConstructor
public class DataClassificationService {

    public Classifier J48(DatasetEntity dataset, String[] options) throws Exception{
        dataset.getDataset().setClassIndex(dataset.getAttributesNumber() - 1);
        J48 j48 = new J48();
        if(options != null)
            j48.setOptions(options);
        j48.buildClassifier(dataset.getDataset());
        return j48;
    }

    public Classifier NaiveBayes(DatasetEntity dataset, String[] options) throws Exception{
        dataset.getDataset().setClassIndex(dataset.getAttributesNumber() - 1);
        NaiveBayes naiveBayes = new NaiveBayes();
        if(naiveBayes != null)
            naiveBayes.setOptions(options);
        naiveBayes.buildClassifier(dataset.getDataset());
        return naiveBayes;
    }
  
}
