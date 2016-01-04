package com.jpay.videograms.tasks;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import com.jpay.mvcvm.utils.JLog;
import com.jpay.videograms.Constants;
import com.jpay.videograms.models.ContactList;
import com.jpay.videograms.utils.Utils;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A background task responsible for the deserialization of the contacts_vg file, bringing it into memory.
 */
public class DeserializeContactFileTask extends JBaseTask<Void, Void, ContactList> {

	public DeserializeContactFileTask(TaskNotifier notifier) {
		super(notifier);
	}
	
	public Object DeserializeCategoriesFile() throws JsonSyntaxException, IOException {
        File file = Utils.getVideogramDataFile();
        if (file != null) {
            String jsonString = FileUtils.readFileToString(file);
            JsonDeserializer<Date> deser = new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonElement json, Type typeOfT,
                                        JsonDeserializationContext context) throws JsonParseException {
                    try {
                        SimpleDateFormat dateFormat = new SimpleDateFormat("\"yyyy-MM-dd'T'HH:mm:ss.SSS\"");
                        return json == null ? null : dateFormat.parse(json.toString());
                    } catch(ParseException ex) {
                        JLog.printStrackTrace(ex);
                    }

                    return null;
                }
            };

            Gson gson = new GsonBuilder().registerTypeAdapter(Date.class, deser).create();
            return gson.fromJson(jsonString, ContactList.class);
        }

        throw new IOException(Constants.CONTACT_VG_FILE_NAME + " file unreadable or missing");
	}

	@Override
	protected boolean performParameterValidation() {
		return false;
	}

	@Override
	protected String getClassName() {
		return DeserializeContactFileTask.class.getSimpleName();
	}

	@Override
	protected Object run(Object... params) throws Exception {
		return this.DeserializeCategoriesFile();
	}

}
