package com.concon.talkabout.talkabout.service;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gconte on 3/16/15.
 */
public class ParserService {

    private static final String ns = " ";

    private XmlPullParser xmlPullParser;

    public List<String> parseXml(Integer talkLevel, InputStream xml) throws XmlPullParserException, IOException {
        xmlPullParser = Xml.newPullParser();
        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
        xmlPullParser.setInput(xml, null);
        xmlPullParser.nextTag();
        return readFeed(xmlPullParser, talkLevel);
    }

    private List readFeed(XmlPullParser parser, Integer talkLevel) throws XmlPullParserException, IOException {
        List<String> talks = new ArrayList();

        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if(talkLevel == 3) {
                getLevelThreeTalks(parser, talks);
            } else if (talkLevel == 2) {
                parser.require(XmlPullParser.START_TAG, ns, "talks3");
                skip(parser);
                getLevelTwoTalks(parser, talks);
            } else {
                parser.require(XmlPullParser.START_TAG, ns, "talks3");
                skip(parser);
                parser.require(XmlPullParser.START_TAG, ns, "talks2");
                skip(parser);
                getLevelOneTalks(parser, talks);
            }
        }
        return talks;
    }

    private List<String> getLevelThreeTalks(XmlPullParser parser, List<String> talks) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "talks3");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            talks.add(read(parser));
        }
        return getLevelTwoTalks(parser, talks);
    }

    private List<String> getLevelTwoTalks(XmlPullParser parser, List<String> talks) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "talks2");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            talks.add(read(parser));
        }
        return getLevelOneTalks(parser, talks);
    }

    private List<String> getLevelOneTalks(XmlPullParser parser, List<String> talks) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "talks1");

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            talks.add(read(parser));
        }
        return talks;
    }

    private String read(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "talk");
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "talk");
        return text;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}