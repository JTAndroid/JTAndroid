/*
 * Copyright (C) 2015 Emanuel Moecklin
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.utils.note.rteditor.effects;

import java.util.ArrayList;
import java.util.List;

import android.text.Spannable;
import android.util.SparseIntArray;

import com.utils.note.rteditor.RTEditText;
import com.utils.note.rteditor.spans.IntendationSpan;
import com.utils.note.rteditor.spans.NumberSpan;
import com.utils.note.rteditor.spans.ParagraphSpan;
import com.utils.note.rteditor.utils.Paragraph;
import com.utils.note.rteditor.utils.Selection;

/**
 * Numbering.
 * <p>
 * NumberSpans are always applied to whole paragraphs and each paragraphs gets its "own" NumberSpan (1:1).
 * Editing might violate this rule (deleting a line feed merges two paragraphs).
 * Each call to applyToSelection will make sure that each paragraph has again its own NumberSpan
 * (call applyToSelection(RTEditText, null, null) and all will be good again).
 */
public class NumberEffect extends LeadingMarginEffect {

    @Override
    public void applyToSelection(final RTEditText editor, Selection selectedParagraphs, Boolean enable) {
        final Spannable str = editor.getText();

        List<ParagraphSpan> spans2Process = new ArrayList<ParagraphSpan>();

        int lineNr = 1;
        SparseIntArray indentations = new SparseIntArray();
        SparseIntArray numbers = new SparseIntArray();
        for (Paragraph paragraph : editor.getParagraphs()) {

			/*
             * We need to know the indentation for each paragraph to be able
			 * to determine which paragraphs belong together (same indentation)
			 */
            int currentIndentation = 0;
            Object[] indentationSpans = Effects.INDENTATION.getCleanSpans(str, paragraph);
            if (indentationSpans.length > 0) {
                for (Object span : indentationSpans) {
                    currentIndentation += ((IntendationSpan) span).getLeadingMargin();
                }
            }
            indentations.put(lineNr, currentIndentation);

			/*
			 * Find existing NumberSpans for this paragraph
			 */
            Object[] existingSpans = getCleanSpans(str, paragraph);
            boolean hasExistingSpans = existingSpans != null && existingSpans.length > 0;
            if (hasExistingSpans) {
                for (Object span : existingSpans) {
                    spans2Process.add(new ParagraphSpan(span, paragraph, true));
                }
            }

			/*
			 * If the paragraph is selected then we sure have a number
			 */
            boolean hasNumber = paragraph.isSelected(selectedParagraphs) ? enable : hasExistingSpans;

			/*
			 * If we have a number then apply a new span
			 */
            if (hasNumber) {
                // let's determine the number for this paragraph
                int nr = 1;
                for (int line = 1; line < lineNr; line++) {
                    int indentation = indentations.get(line);
                    int number = numbers.get(line);
                    if (indentation < currentIndentation) {
                        // 1) less indentation -> number 1
                        nr = 1;
                    } else if (indentation == currentIndentation) {
                        // 2) same indentation + no numbering -> number 1
                        // 3) same indentation + numbering -> increment number
                        nr = number == 0 ? 1 : number + 1;
                    }
                }
                numbers.put(lineNr, nr);

                int gap = getLeadingMargingIncrement();
                NumberSpan numberSpan = new NumberSpan(nr++, gap, paragraph.isEmpty(), paragraph.isFirst(), paragraph.isLast());
                spans2Process.add(new ParagraphSpan(numberSpan, paragraph, false));

                // if the paragraph has bullet spans, then remove it
                Effects.BULLET.findSpans2Remove(str, paragraph, spans2Process);
            }

            lineNr++;
        }

        // add or remove spans
        for (final ParagraphSpan spanDef : spans2Process) {
            spanDef.process(str);
        }
    }

    @Override
    protected NumberSpan[] getSpans(Spannable str, Selection selection) {
        return str.getSpans(selection.start(), selection.end(), NumberSpan.class);
    }
}