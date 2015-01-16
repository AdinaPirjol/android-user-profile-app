package com.app.md_hw.InterestsFragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.md_hw.R;
import com.parse.FindCallback;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProgrammingLanguagesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProgrammingLanguagesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProgrammingLanguagesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProgrammingLanguagesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProgrammingLanguagesFragment newInstance(String param1, String param2) {
        ProgrammingLanguagesFragment fragment = new ProgrammingLanguagesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ProgrammingLanguagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final LinearLayout linearLayout = new LinearLayout(getActivity());

        ParseUser user = ParseUser.getCurrentUser();
        String username = user.getUsername();

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Interests");
        query.whereEqualTo("username", username);

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> parseObjects, com.parse.ParseException e) {
                if (e == null) {
                    String text = "";
                    String m = "Select favorite programming language or I will return NullPointerException";

                    TextView editText = new TextView(getActivity());
                    editText.setId(R.id.prog_lang); //Set id to remove in the future.
                    editText.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT));

                    for (ParseObject p : parseObjects) {
                        text = (String) p.get("interests");
                    }

                    try {
                        JSONObject json_text = new JSONObject(text);
                        if(json_text.has("prog-lang")) {
                            m = "Programming Languages:\n";
                            JSONObject prog_lang = json_text.getJSONObject("prog-lang");

                            if(prog_lang.has("c")){
                                m += "-C/C++/C#/C-anything\n";
                            }
                            if(prog_lang.has("java")){
                                m+= "-Java\n";
                            }
                            if(prog_lang.has("web-dev")){
                                m+= "-Web Dev\n";
                            }

                            if(prog_lang.has("what-is-that")){
                                m+= "-What is that??\n";
                            }

                            editText.setText(m);
                        }
                    } catch (JSONException e1) {
                        e1.printStackTrace();
                    }


                    TextView fragmProgLang = (TextView) getActivity().findViewById(R.id.textFragmProgLang);
                    fragmProgLang.setText(m);
                    editText.setText(m);

                    try{
                        linearLayout.addView(editText);
                        getActivity().addContentView(linearLayout, new
                                LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT));


                    }catch(Exception ex){
                        ex.printStackTrace();
                    }
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Could not retrieve interests", Toast.LENGTH_LONG).show();
                }
            }
        });

        // Inflate the layout for this fragment with the programatically added layout
        // with the appropiate content
        View v = inflater.inflate(R.layout.fragment_programming_languages, linearLayout);
        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }

}
