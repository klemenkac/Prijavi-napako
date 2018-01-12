package com.example.kac.prijavinapako;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.weiwangcn.betterspinner.library.material.MaterialBetterSpinner;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import weka.classifiers.Evaluation;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.trees.DecisionStump;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.LMT;
import weka.classifiers.trees.REPTree;
import weka.classifiers.trees.lmt.LMTNode;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;


/**
 * Created by xklem on 13. 03. 2017.
 */

public class ActivityWeka extends AppCompatActivity {


    String [] danList = {"ponedeljek", "torek","sreda","cetrtek","petek","sobota","nedelja"};
    String [] krajList = {"Maribor","Ljubljana","Koper"};
    String [] domList = {"1","2","3","4","5","6","7","8","9","10","11","12","13"};
    String [] nadstropjeList = {"prvo","drugo","tretje","cetrto","peto","sesto"};
    String [] krivdaList = {"da","ne"};
    String [] nujnostList = {"urgentno","navadno"};

    String [] klasifList = {"J48","DecisionStump","REPTree"};

    EditText folds;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weka);

        ArrayAdapter<String> arrayDan = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,danList);
        final MaterialBetterSpinner danS = (MaterialBetterSpinner)findViewById(R.id.sDan);
        danS.setAdapter(arrayDan);

        ArrayAdapter<String> arrayKraj = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,krajList);
        final MaterialBetterSpinner krajS = (MaterialBetterSpinner)findViewById(R.id.sKraj);
        krajS.setAdapter(arrayKraj);

        ArrayAdapter<String> arrayDom = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,domList);
        final MaterialBetterSpinner domS = (MaterialBetterSpinner)findViewById(R.id.sDom);
        domS.setAdapter(arrayDom);

        ArrayAdapter<String> arrayNad = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,nadstropjeList);
        final MaterialBetterSpinner nadS = (MaterialBetterSpinner)findViewById(R.id.sNadstropje);
        nadS.setAdapter(arrayNad);

        ArrayAdapter<String> arrayKrivda = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,krivdaList);
        final MaterialBetterSpinner krivdaS = (MaterialBetterSpinner)findViewById(R.id.sKrivda);
        krivdaS.setAdapter(arrayKrivda);

        ArrayAdapter<String> arrayNuj = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,nujnostList);
        final MaterialBetterSpinner nujnS = (MaterialBetterSpinner)findViewById(R.id.sNujnost);
        nujnS.setAdapter(arrayNuj);

        ArrayAdapter<String> arrayKlas = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line,klasifList);
        final MaterialBetterSpinner klasS = (MaterialBetterSpinner)findViewById(R.id.sKlasifi);
        klasS.setAdapter(arrayKlas);

        final Button button = findViewById(R.id.racunaj);

        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                try {
                    // Testna 'množica' (1 primer)
                    FastVector fvNominalVal;
                    fvNominalVal = new FastVector(3);//ponedeljek,torek,sreda,cetrtek,petek,sobota,nedelja
                    fvNominalVal.addElement("ponedeljek");
                    fvNominalVal.addElement("torek");
                    fvNominalVal.addElement("sreda");
                    fvNominalVal.addElement("cetrtek");
                    fvNominalVal.addElement("petek");
                    fvNominalVal.addElement("sobota");
                    fvNominalVal.addElement("nedelja");
                    Attribute Attribute1 = new Attribute("Dan napake", fvNominalVal);

                    fvNominalVal = new FastVector(3);
                    fvNominalVal.addElement("Maribor");
                    fvNominalVal.addElement("Ljubljana");
                    fvNominalVal.addElement("Koper");
                    Attribute Attribute2 = new Attribute("Kraj", fvNominalVal);

                    fvNominalVal = new FastVector(13);
                    for(int i=1;i<14;i++)
                        fvNominalVal.addElement(String.valueOf(i));
                    Attribute Attribute3 = new Attribute("Studentski dom", fvNominalVal);

                    fvNominalVal = new FastVector(6);
                    fvNominalVal.addElement("prvo");
                    fvNominalVal.addElement("drugo");
                    fvNominalVal.addElement("tretje");
                    fvNominalVal.addElement("cetrto");
                    fvNominalVal.addElement("peto");
                    fvNominalVal.addElement("sesto");
                    Attribute Attribute4 = new Attribute("Nadstropje", fvNominalVal);

                    fvNominalVal = new FastVector(2);
                    fvNominalVal.addElement("da");
                    fvNominalVal.addElement("ne");
                    Attribute Attribute5 = new Attribute("Lastna krivda", fvNominalVal);

                    fvNominalVal = new FastVector(2);
                    fvNominalVal.addElement("urgentno");
                    fvNominalVal.addElement("navadno");
                    Attribute Attribute6 = new Attribute("Nujnost", fvNominalVal);

                    FastVector fvClassVal = new FastVector(4);
                    fvClassVal.addElement("elektro");
                    fvClassVal.addElement("oprema");
                    fvClassVal.addElement("vodovod");
                    fvClassVal.addElement("internet");
                    Attribute ClassAttribute = new Attribute("Class", fvClassVal);

                    FastVector fvWekaAttributes = new FastVector(7);
                    fvWekaAttributes.addElement(Attribute1);
                    fvWekaAttributes.addElement(Attribute2);
                    fvWekaAttributes.addElement(Attribute3);
                    fvWekaAttributes.addElement(Attribute4);
                    fvWekaAttributes.addElement(Attribute5);
                    fvWekaAttributes.addElement(Attribute6);
                    fvWekaAttributes.addElement(ClassAttribute);

                    // Create an empty training set
                    Instances isTrainingSet = new Instances("Rel", fvWekaAttributes, 10);
                    // Set class index
                    isTrainingSet.setClassIndex(6);

                    // Create the instance
                    Instance iExample = new DenseInstance(7);
                    iExample.setValue((Attribute)fvWekaAttributes.elementAt(0), danS.getText().toString());
                    iExample.setValue((Attribute)fvWekaAttributes.elementAt(1), krajS.getText().toString());
                    iExample.setValue((Attribute)fvWekaAttributes.elementAt(2), domS.getText().toString());
                    iExample.setValue((Attribute)fvWekaAttributes.elementAt(3), nadS.getText().toString());
                    iExample.setValue((Attribute)fvWekaAttributes.elementAt(4), krivdaS.getText().toString());
                    iExample.setValue((Attribute)fvWekaAttributes.elementAt(5), nujnS.getText().toString());
                    // add the instance
                    isTrainingSet.add(iExample);

                    // Učna množica
                    InputStream is = getBaseContext().getAssets().open("napake.arff");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    Instances data = new Instances(reader);
                    reader.close();
                    // setting class attribute
                    data.setClassIndex(data.numAttributes() - 1);

                    Evaluation eval;
                    double prediction=0.0;
                    if(klasS.getText().toString().equals("J48")){
                        String[] options = new String[1];
                        options[0] = "-U";            // unpruned tree
                        J48 tree = new J48();         // new instance of tree

                        tree.setOptions(options);     // set the options
                        tree.buildClassifier(data);   // build classifier

                        prediction = tree.classifyInstance(isTrainingSet.instance(0));

                        eval = new Evaluation(isTrainingSet);
                        eval.evaluateModel(tree, data);
                    }
                    else if(klasS.getText().toString().equals("DecisionStump")){
                        String[] options = new String[1];
                        options[0] = "-U";            // unpruned tree
                        DecisionStump tree = new DecisionStump();         // new instance of tree

                        //tree.setOptions(options);     // set the options
                        tree.buildClassifier(data);   // build classifier

                        prediction = tree.classifyInstance(isTrainingSet.instance(0));

                        eval = new Evaluation(isTrainingSet);
                        eval.evaluateModel(tree, data);
                    }
                    else {
                        String[] options = new String[1];
                        //options[0] = "-U";            // unpruned tree
                        REPTree tree = new REPTree();         // new instance of tree

                       // tree.setOptions(options);     // set the options
                        tree.buildClassifier(data);   // build classifier

                        prediction = tree.classifyInstance(isTrainingSet.instance(0));

                        eval = new Evaluation(isTrainingSet);
                        eval.evaluateModel(tree, data);

                    }

                    if(prediction==0.0)
                        Toast.makeText(ActivityWeka.this, "Napoved: Elektro\n" + eval.toSummaryString(), Toast.LENGTH_SHORT).show();
                    else if(prediction==1.0)
                        Toast.makeText(ActivityWeka.this, "Napoved: Oprema\n" + eval.toSummaryString() , Toast.LENGTH_SHORT).show();
                    else if(prediction==2.0)
                        Toast.makeText(ActivityWeka.this, "Napoved: Vodovod\n" + eval.toSummaryString(), Toast.LENGTH_SHORT).show();
                    else if(prediction==3.0)
                        Toast.makeText(ActivityWeka.this, "Napoved: Internet\n" + eval.toSummaryString() , Toast.LENGTH_SHORT).show();
                    else
                        Toast.makeText(ActivityWeka.this, String.valueOf(prediction) , Toast.LENGTH_SHORT).show();
                }
                catch (Exception e){
                    Toast.makeText(ActivityWeka.this, "Nekaj je šlo narobe..." , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}