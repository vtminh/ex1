package cse.vn.currency;

import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class MainActivity extends AppCompatActivity {
    /**
     * Create a new textview array to display the results
     */
    Spinner spin;
    LinearLayout layout;
    ListView listView;
    ArrayList<CurrencyInfo> listCI = new ArrayList<>();
    ArrayList<String> listCode = new ArrayList<>();

    TextView tvUnit;
    Button btnChange;
    int unit_index = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /** Create a new layout to display the view */
        layout = (LinearLayout) findViewById(R.id.activity_main);
        listView = (ListView) findViewById(R.id.listView);
        tvUnit = (TextView) findViewById(R.id.tvUnit);
        btnChange = (Button) findViewById(R.id.btnChange);
        final EditText editText = (EditText) findViewById(R.id.etMoney) ;
        spin = (Spinner) findViewById(R.id.spinner);

        btnChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currency = Float.parseFloat(editText.getText().toString()) + " " + listCI.get(unit_index).code;
                float result = Float.parseFloat(editText.getText().toString()) * listCI.get(unit_index).transfer;
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("Kết quả")
                        .setMessage(currency + " = " + String.valueOf(result) + " VND")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // continue with delete
                            }
                        })
                        .show();
            }
        });







        spin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                tvUnit.setText(listCI.get(position).code);
                unit_index = position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        GetCurrencyFromAPI getCurrencyFromAPI = new GetCurrencyFromAPI(this);
        getCurrencyFromAPI.execute("http://www.vietcombank.com.vn/exchangerates/ExrateXML.aspx");

    }



    class GetCurrencyFromAPI extends AsyncTask<String, Void, NodeList> {

        Context mContext;

        GetCurrencyFromAPI(Context context) {
            this.mContext = context;

        }

        protected NodeList doInBackground(String... urls) {
            URL url;
            try {
                url = new URL(urls[0]);
                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document doc = db.parse(new InputSource(url.openStream()));
                doc.getDocumentElement().normalize();

                NodeList nodeList = doc.getElementsByTagName("Exrate");
                return nodeList;
            } catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace();
            }

            return null;

        }

        protected void onPostExecute(NodeList nodeList) {
            // TODO: check this.exception
            // TODO: do something with the feed
            super.onPostExecute(nodeList);

            /** Assign textview array lenght by arraylist size */


            for (int i = 0; i < nodeList.getLength(); i++) {

                Node node = nodeList.item(i);

                Element fstElmnt = (Element) node;

//                NodeList nameList = fstElmnt.getElementsByTagName("name");
//                Element nameElement = (Element) nameList.item(0);
//                String value =  nameElement.getChildNodes().item(0).getNodeValue();
//                name[i].setText("Name = " + value);
//
//                NodeList websiteList = fstElmnt.getElementsByTagName("website");
//                Element websiteElement = (Element) websiteList.item(0);
//                websiteList = websiteElement.getChildNodes();
//                website[i].setText("Website = " + ((Node) websiteList.item(0)).getNodeValue());
//
//                category[i].setText("Website Category = " + websiteElement.getAttribute("category"));

                String code = fstElmnt.getAttribute("CurrencyCode");
                String name = fstElmnt.getAttribute("CurrencyName");
                float transfer = Float.parseFloat(fstElmnt.getAttribute("Transfer"));


                CurrencyInfo currencyInfo = new CurrencyInfo(code, name, 0, 0, transfer);

                listCI.add(currencyInfo);
                listCode.add(code);

            }


            /** Set the layout view to display */
            setContentView(layout);

            MyListViewAdapter adapter = new MyListViewAdapter(MainActivity.this, R.layout.list_view_item, listCI);
            listView.setAdapter(adapter);

            ArrayAdapter<String> spinner_adapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout
                    .simple_spinner_item,
                    listCode);
            spinner_adapter.setDropDownViewResource
                    (android.R.layout.simple_spinner_dropdown_item);
            //Thiết lập adapter cho Spinner
            spin.setAdapter(spinner_adapter);
        }
    }

}
