package edu.umass.cs.surveyman.output;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class ClassifiedRespondentsStruct extends ArrayList<ClassificationStruct> {

    public String jsonize()
    {
        List<String> strings = new ArrayList<String>();
        for (ClassificationStruct classificationStruct : this) {
            strings.add(classificationStruct.jsonize());
        }
        return String.format("[ %s ] ", StringUtils.join(strings, ", "));
    }

    @Override
    public String toString(){
        List<String> strings = new ArrayList<String>();
        int numvalid = 0;
        int n = this.size();
        for (ClassificationStruct classificationStruct : this){
            numvalid += classificationStruct.valid ? 1 : 0;
            strings.add(classificationStruct.toString());
        }
        return String.format("Response classifications (%d valid, %f perc. of sample)\n" +
                "srid\tclassifiername\tnumanswered\tscore\tthreshold\tisvalid\n",
                numvalid, ((double) numvalid) / n) +
                StringUtils.join(strings, "\n");
    }

}
