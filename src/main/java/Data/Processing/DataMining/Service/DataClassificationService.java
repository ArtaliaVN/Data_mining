package Data.Processing.DataMining.Service;

import java.util.Random;

import org.springframework.stereotype.Service;
import weka.classifiers.trees.J48;
import weka.core.Capabilities;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import Data.Processing.DataMining.Entity.DatasetEntity;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@NoArgsConstructor
public class DataClassificationService {

    public Evaluation J48(DatasetEntity dataset, int folds) throws Exception{
        dataset.getDataset().setClassIndex(dataset.getInstancesNumber() - 1);
        J48 j48 = new J48();
        j48.buildClassifier(dataset.getDataset());
        Evaluation eval = new Evaluation(dataset.getDataset());
        eval.crossValidateModel(j48, dataset.getDataset(), folds, new Random());
        return eval;
    }

    public Capabilities J48(DatasetEntity dataset) throws Exception{
        dataset.getDataset().setClassIndex(dataset.getInstancesNumber() - 1);
        J48 j48 = new J48();
        j48.buildClassifier(dataset.getDataset());
        return j48.getCapabilities();
    }

    public Capabilities NaiveBayes(DatasetEntity dataset) throws Exception{
        dataset.getDataset().setClassIndex(dataset.getInstancesNumber() - 1);
        NaiveBayes naiveBayes = new NaiveBayes();
        naiveBayes.buildClassifier(dataset.getDataset());
        return naiveBayes.getCapabilities();
    }
  
}
