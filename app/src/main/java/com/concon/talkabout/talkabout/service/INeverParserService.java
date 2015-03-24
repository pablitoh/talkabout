package com.concon.talkabout.talkabout.service;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * Created by gconte on 3/19/15.
 */
public class INeverParserService extends ParserService {

    public void parseInnerCustom(XmlPullParser parser, int talkLevel, String tag, List<String> data) throws XmlPullParserException, IOException {



        for (int i = 0; i < talkLevel; i++) {
            if(i > 0) {
                parser.nextTag();
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            Integer tagLevel = Integer.valueOf(name.replaceAll(tag, ""));
            if (tagLevel <= talkLevel) {
                parser.require(XmlPullParser.START_TAG, ns, name);
                while (parser.next() != XmlPullParser.END_TAG) {
                    if (parser.getEventType() != XmlPullParser.START_TAG) {
                        continue;
                    }
                    data.add(read(parser, "entry"));
                }
            } else {
                skip(parser);
            }
        }
    }
}
