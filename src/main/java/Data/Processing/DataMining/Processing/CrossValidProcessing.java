package Data.Processing.DataMining.Processing;

import Data.Processing.DataMining.Entity.DatasetEntity;
import weka.classifiers.Evaluation;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.trees.RandomForest;
import weka.core.Instances;
import weka.core.Option;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.*;

import java.util.Random;

public class CrossValidProcessing {
    public CrossValidProcessing() throws Exception {

        DatasetEntity entity = new DatasetEntity("C:\\Users\\ADMIN\\Downloads\\DataMining\\src\\main\\java\\Data\\Processing\\DataMining\\TestingFile\\cleansed.csv");
        System.out.println(entity.getDataset().toSummaryString());


//        Standardize standardize = new Standardize();
//        standardize.setInputFormat(entity.getDataset());
//        entity.setDataset(Filter.useFilter(entity.getDataset(), standardize));
//
        entity.getDataset().setClassIndex(2);
        Instances instances = entity.getDataset();
        System.out.println("Class index is "+ instances.classAttribute());

        Discretize discretize = new Discretize();
        discretize.setOptions(Utils.splitOptions("-unset-class-temporarily"));
        discretize.setInputFormat(instances);
        instances = Filter.useFilter(instances, discretize);


        System.out.println(instances.toSummaryString());
        int folds = 10;
        int seed = 1;

        RandomForest tree = new RandomForest();
        Random rand = new Random(seed);


        instances.randomize(rand);
        instances.stratify(folds);

        for(int i = 0; i < folds; i++) {
            Evaluation eval = new Evaluation(instances);

            Instances trainingInstances = instances.trainCV(folds, i);
            Instances testingInstances = instances.testCV(folds, i);

            tree.buildClassifier(trainingInstances);
            eval.evaluateModel(tree, testingInstances);

            System.out.println(eval.toMatrixString("=== Confusion matrix for fold " + (i+1) + "/" + folds + " ="));
            System.out.println("Correct: " + eval.pctCorrect());
            System.out.println("Incorrect: " + eval.pctIncorrect());
            System.out.println("Precision: " + eval.precision(2));
            System.out.println("Mean absolute error: " + eval.meanAbsoluteError());
            System.out.println("Root mean squared error: " + eval.rootMeanPriorSquaredError());
            System.out.println("Relative absolute error: " + eval.relativeAbsoluteError());
        }
    }
}
