package com.destin.pendututo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout container;
    private Button btn_send;
    private TextView lettre_tapees;
    private ImageView image;
    private EditText et_letter;
    private String word;
    private int found;
    private int error;
    private List<Character> listOfLetters = new ArrayList<>();
    private boolean win;
    private List<String > wordList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        container = findViewById(R.id.word_container);
        btn_send = findViewById(R.id.btn_send);
        et_letter = findViewById(R.id.et_letter);
        image = findViewById(R.id.iv_pendu);
        lettre_tapees = findViewById(R.id.tv_lettres_tapees);

        initGame();
        btn_send.setOnClickListener(this);
    }

    public void initGame() {
        word = generateWord();
        win = false;
        error = 0;
        found = 0;
        lettre_tapees.setText("");
        image.setBackgroundResource(R.drawable.first);
        listOfLetters= new ArrayList<>();
        container.removeAllViews();
        for (int i = 0; i < word.length(); i++) {
            TextView oneLetter = (TextView) getLayoutInflater().inflate(R.layout.textview, null);
            container.addView(oneLetter);
        }


    }

    @Override
    public void onClick(View view) {

        /*Toast toast =  Toast.makeText(getApplicationContext(),"Salut",Toast.LENGTH_LONG);
        toast.show();

        System.out.println(toast.getView());

      System.out.println("Bien joué");*/


        String letterFromInput = et_letter.getText().toString().toUpperCase();
        et_letter.setText("");

        if(letterFromInput.length()>0){
            if(!letterAlreadyUsed(letterFromInput.charAt(0),listOfLetters)){
                listOfLetters.add(letterFromInput.charAt(0));
                checkIfLetterIsInWord(letterFromInput,word);
            }
        }
        if(found == word.length()){
            win=true;
            //Toast.makeText(getApplicationContext(),"Victoire",Toast.LENGTH_LONG).show();
            createDialog(win);
        }

        if(!word.contains(letterFromInput)){
            error++;
        }
        setImage(error);
        if(error==6){
            win=false;
           //Toast.makeText(getApplicationContext(),"PERDU",Toast.LENGTH_LONG).show();
            createDialog(win);
        }
        showAllLetters(listOfLetters);

    }

    public boolean letterAlreadyUsed(char a, List<Character> listOfLetters) {
        for (int i = 0; i < listOfLetters.size(); i++) {
            if (listOfLetters.get(i) == a) {
                Toast.makeText(getApplicationContext(), "Vous avez déjà entré cette lettre", Toast.LENGTH_LONG).show();
                return true;
            }
        }
        return false;
    }

    public void checkIfLetterIsInWord(String letter,String word){
        for(int i =0;i<word.length();i++){
            if(letter.equals(String.valueOf(word.charAt(i)))){
                TextView tv = (TextView) container.getChildAt(i);
                tv.setText(String.valueOf(word.charAt(i)));
                found++;

            }
        }
    }
    public void showAllLetters(List<Character> listOfLetters){
        String chaine= "";

        for(int i=0;i<listOfLetters.size();i++){
            chaine += listOfLetters.get(i)+"\n";
        }
        if(!chaine.equals("")){
            lettre_tapees.setText(chaine);
        }

    }
    public void setImage(int error){

        switch(error){
            case 1:
                image.setBackgroundResource(R.drawable.second);
                break;
            case 2:
                image.setBackgroundResource(R.drawable.third);
                break;
            case 3:
                image.setBackgroundResource(R.drawable.fourth);
                break;
            case 4:
                image.setBackgroundResource(R.drawable.fifth);
                break;
            case 5:
                image.setBackgroundResource(R.drawable.sixth);
                break;
            case 6:
                image.setBackgroundResource(R.drawable.seventh);
                break;

        }

    }

    public void createDialog(boolean win){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Vous avez gagné !");
        if(!win){
            builder.setTitle("Vous avez perdu !");
            builder.setMessage("Le mot à trouver était : "+word);

        }
        builder.setPositiveButton(getResources().getString(R.string.rejouer), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                initGame();
            }
        });
        builder.create().show();
    }

    public List<String> getListOfWords(){
        try{
            BufferedReader buffer = new BufferedReader(new InputStreamReader(getAssets().open("pendu_liste.txt")));
            String line;
            while ((line = buffer.readLine())!=null){
                wordList.add(line);
            }
            buffer.close();
        }catch(IOException e){
            e.printStackTrace();
        }
        return wordList;
    }

    public String generateWord(){

        wordList=getListOfWords();
        int random = (int)(Math.floor(Math.random()*wordList.size()));
        String word = wordList.get(random).trim();

        return word;

    }


}

