import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MessageExchange {

    private JSONParser jsonParser = new JSONParser();

    public String getToken(int index) {
        Integer number = index * 8 + 11;
        return "TN" + number + "EN";
    }

    public int getIndex(String token) {
        return (Integer.valueOf(token.substring(2, token.length() - 2)) - 11) / 8;
    }

    public String getServerResponse(List<Message> messages) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("messages", messages);
        jsonObject.put("token", getToken(messages.size()));
        return jsonObject.toJSONString();
    }

    public String getClientSendMessageRequest(Message message) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("message", message.toString());
        return jsonObject.toJSONString();
    }

    public Message getClientMessage(InputStream inputStream, Integer id) throws ParseException {
        JSONObject json1 = getJSONObject(inputStreamToString(inputStream));
        return new Message((String) json1.get("user"), id, (String) json1.get("text"));
    }

    /*
    public String getClientMessage(InputStream inputStream) throws ParseException {
        return (String) getJSONObject(inputStreamToString(inputStream)).get("message");
    }
    */
    public String getClientId(InputStream inputStream) throws ParseException {
        return (String) getJSONObject(inputStreamToString(inputStream)).get("id");
    }

    public Message getClientEditInfo(InputStream inputStream) throws ParseException {
        return  (new  Message()).editInfo(getJSONObject(inputStreamToString(inputStream)));
    }

    public JSONObject getJSONObject(String json) throws ParseException {
        return (JSONObject) jsonParser.parse(json.trim());
    }

    public String inputStreamToString(InputStream in) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        try {
            while ((length = in.read(buffer)) != -1) {
                baos.write(buffer, 0, length);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new String(baos.toByteArray());
    }
}
