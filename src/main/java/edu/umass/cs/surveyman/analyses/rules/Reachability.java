package edu.umass.cs.surveyman.analyses.rules;

import edu.umass.cs.surveyman.analyses.AbstractRule;
import edu.umass.cs.surveyman.qc.Interpreter;
import edu.umass.cs.surveyman.survey.Block;
import edu.umass.cs.surveyman.survey.Survey;
import edu.umass.cs.surveyman.survey.exceptions.SurveyException;
import edu.umass.cs.surveyman.survey.exceptions.UnreachableBlockException;
import edu.umass.cs.surveyman.qc.QCMetrics;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Reachability extends AbstractRule {

    public Reachability() {
        AbstractRule.registerRule(this);
    }

    @Override
    public void check(Survey survey) throws SurveyException {
        // only need to check stationary top-level blocks
        List<Block> stationaryBlocks = Interpreter.partitionBlocks(survey).get(false);
        List<List<Block>> dag = QCMetrics.getDag(stationaryBlocks);
        Set<Block> allVisited = new HashSet<Block>();
        for (List<Block> path : dag)
            for (Block b : path)
                allVisited.add(b);
        for (Block b : stationaryBlocks)
            if (!allVisited.contains(b))
                throw new UnreachableBlockException(b);
    }
}
