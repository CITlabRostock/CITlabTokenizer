/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uros.citlab.tokenizer.categorizer;

import de.uros.citlab.tokenizer.interfaces.ICategorizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * categorizes characters by their general category specified by the unicode
 * properties. Therefore, the main category is taken ("L" for letters, "N" for
 * numbers, "S" for symbols,...). Delimiters are all codepoints with category
 * "Z". All codepoints are isolated tokens EXCEPT codepoints with general
 * categories "L" and "N".
 *
 * @author gundram
 */
public class CategorizerWordMergeGroups implements ICategorizer {

    public static Logger LOG = LoggerFactory.getLogger(CategorizerWordMergeGroups.class.getName());

    @Override
    public String getCategory(char c) {
        if (c == '\n' || c == '\t') {
            return "Z";
        }
        if (c == 173) {
            return "P";
        }
        String cat = CategoryUtils.getCategoryGeneral(c);
        switch (cat) {
            case "M":
                return "L";
            case "S":
                return "P";
            case "C":
                switch (CategoryUtils.getCategory(c)) {
                    case "Cs":
                        LOG.warn("cannot handle surrogates - character ''{}'' with decimal-value {}. Assume to be a letter.", c, (int) c);
//                        throw new RuntimeException("cannot handle surrogates - character '" + c + "' with decimal-value " + ((int) c) + ".");
                    case "Co":
                        return "L";
                    case "Cc":
                        throw new RuntimeException("cannot handle control characters '" + c + "' with decimal-value " + ((int) c) + ".");
                    case "Cf":
                        throw new RuntimeException("cannot handle format characters '" + c + "' with decimal-value " + ((int) c) + ".");
                    default:
                        throw new RuntimeException("cannot handle characters '" + c + "' with decimal-value " + ((int) c) + ".");

                }
            default:
                return cat;
        }
    }

    @Override
    public boolean isDelimiter(char c) {
        return "Z".equals(getCategory(c));
    }

    @Override
    public boolean isIsolated(char c) {
        switch (getCategory(c)) {
            case "L":
            case "N":
                return false;
            default:
                return true;
        }
    }

}
