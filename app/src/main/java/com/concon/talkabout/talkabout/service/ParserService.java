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

    private static final String ns = null;

    private XmlPullParser xmlPullParser;

    public List<String> parseXml(int talkLevel, InputStream xml, String tag) throws XmlPullParserException, IOException {
        xmlPullParser = Xml.newPullParser();
        xmlPullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        xmlPullParser.setInput(xml, null);
        xmlPullParser.nextTag();
        return readFeed(xmlPullParser, talkLevel, tag);
    }

    private List readFeed(XmlPullParser parser, int talkLevel, String tag) throws XmlPullParserException, IOException {
        List<String> data = new ArrayList();
        boolean processed = false;
        parser.require(XmlPullParser.START_TAG, ns, "feed");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            if(processed) {
                break;
            }

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
            processed = true;
        }
        return data;
    }

    private String read(XmlPullParser parser, String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, tag);
        String text = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, tag);
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