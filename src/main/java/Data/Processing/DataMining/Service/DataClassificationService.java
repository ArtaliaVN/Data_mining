package Data.Processing.DataMining.Service;

import org.springframework.stereotype.Service;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.rules.OneR;
import weka.classifiers.rules.ZeroR;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
@Data
@NoArgsConstructor
public class DataClassificationService {

    public Classifier J48(Instances dataset, String[] options) throws Exception{
        J48 j48 = new J48();
        if(options != null)
            j48.setOptions(options);
        j48.buildClassifier(dataset);
        return j48;
    }

    public Classifier NaiveBayes(Instances dataset, String[] options) throws Exception{
        NaiveBayes naiveBayes = new NaiveBayes();
        if(options != null)
            naiveBayes.setOptions(options);
        naiveBayes.buildClassifier(dataset);
        return naiveBayes;
    }
  
    public Classifier OneR(Instances dataset, String[] options) throws Exception{
        OneR oner = new OneR();
        if(options != null)
            oner.setOptions(options);
        oner.buildClassifier(dataset);
        return oner;
    }

    public Classifier ZeroR(Instances dataset, String[] options) throws Exception{
        ZeroR zeror = new ZeroR();
        if(options != null)
            zeror.setOptions(options);
        zeror.buildClassifier(dataset);
        return zeror;
    }

    public Classifier BayesNet(Instances dataset, String[] options) throws Exception{
        BayesNet bayesNet = new BayesNet();
        if(options != null)
            bayesNet.setOptions(options);
        bayesNet.buildClassifier(dataset);
        return bayesNet;
    }

    
}