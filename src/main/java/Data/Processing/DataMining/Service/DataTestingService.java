package Data.Processing.DataMining.Service;

import Data.Processing.DataMining.Entity.DatasetEntity;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;

public class DataTestingService {

    public Evaluation NaiveBayes(DatasetEntity trainingDataset, DatasetEntity testingDataset, Classifier classifier) throws Exception{
        Evaluation eval = new Evaluation(trainingDataset.getDataset());
        eval.evaluateModel(classifier, testingDataset.getDataset());
        return eval;
    }

}
