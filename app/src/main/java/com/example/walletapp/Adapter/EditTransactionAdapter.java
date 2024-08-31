package com.example.walletapp.Adapter;

import android.app.DatePickerDialog;
import android.content.Context;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.walletapp.Activity.AddExpenseActivity;
import com.example.walletapp.Constants;
import com.example.walletapp.Model.TransModel;
import com.example.walletapp.NumberTextWatcher;
import com.example.walletapp.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class EditTransactionAdapter extends BaseAdapter {
    private Context context;
    private List<TransModel> item;
    public EditTransactionAdapter(Context context, List<TransModel> item) {
        this.context = context;
        this.item = item;
    }
    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null) {
            listItem = LayoutInflater.from(context).inflate(R.layout.edit_listitem, parent, false);
        }
        TransModel current = item.get(position);

        TextView date_query = listItem.findViewById(R.id.date_query);
        TextView money_query = listItem.findViewById(R.id.money_query);
        TextView type_of_transaction = listItem.findViewById(R.id.type_of_transaction);
        TextView time_of_transaction = listItem.findViewById(R.id.time_of_transaction);
        TextView description_of_transaction = listItem.findViewById(R.id.description_of_transaction);

        String outputString = "";
        String plusOrDevideMoney = "";
        String typeStr = "";
        SimpleDateFormat input = new SimpleDateFormat("EEEE, d 'tháng' M, yyyy", new Locale("vi", "VN"));
        SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = input.parse(current.getDateTrans());
            outputString = output.format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (current.getTypeTrans().equals("revenue_money")) {
            String tempString = current.getMoneyTrans().replace(",", "#");
            tempString = tempString.replace(".", ",");
            String finalString = tempString.replace("#", ".");
            plusOrDevideMoney = "+" + finalString + " VND";
            typeStr = "Khoản thu";
            money_query.setTextColor(0xFF279CC5);
            money_query.setText(plusOrDevideMoney);
        } else if (current.getTypeTrans().equals("expense_money")) {
            String tempString = current.getMoneyTrans().replace(",", "#");
            tempString = tempString.replace(".", ",");
            String finalString = tempString.replace("#", ".");
            plusOrDevideMoney = "-" + finalString + " VND";
            typeStr = "Khoản chi";
            money_query.setTextColor(0xFFE45B65);
            money_query.setText(plusOrDevideMoney);
        } else if (current.getTypeTrans().equals("percentage_money")) {
            typeStr = "Khoản lãi suất";
            if (current.getDetailTypeTrans().equals("Trả lãi")) {
                String tempString = current.getMoneyTrans().replace(",", "#");
                tempString = tempString.replace(".", ",");
                String finalString = tempString.replace("#", ".");
                plusOrDevideMoney = "-" + finalString + " VND";
                money_query.setTextColor(0xFFE45B65);
                money_query.setText(plusOrDevideMoney);
            } else if (current.getDetailTypeTrans().equals("Thu lãi")) {
                String tempString = current.getMoneyTrans().replace(",", "#");
                tempString = tempString.replace(".", ",");
                String finalString = tempString.replace("#", ".");
                plusOrDevideMoney = "+" + finalString + " VND";
                money_query.setTextColor(0xFF279CC5);
                money_query.setText(plusOrDevideMoney);
            }
        } else if (current.getTypeTrans().equals("loan_money")) {
            typeStr = "Khoản vay";
            if (current.getDetailTypeTrans().equals("Cho vay") || current.getDetailTypeTrans().equals("Trả nợ")) {
                String tempString = current.getMoneyTrans().replace(",", "#");
                tempString = tempString.replace(".", ",");
                String finalString = tempString.replace("#", ".");
                plusOrDevideMoney = "-" + finalString+ " VND";
                money_query.setTextColor(0xFFE45B65);
                money_query.setText(plusOrDevideMoney);
            } else if (current.getDetailTypeTrans().equals("Đi vay") || current.getDetailTypeTrans().equals("Thu nợ")) {
                String tempString = current.getMoneyTrans().replace(",", "#");
                tempString = tempString.replace(".", ",");
                String finalString = tempString.replace("#", ".");
                plusOrDevideMoney = "+" + finalString + " VND";
                money_query.setTextColor(0xFF279CC5);
                money_query.setText(plusOrDevideMoney);
            }
        }

        date_query.setText(outputString);
        type_of_transaction.setText("Loại giao dịch: " + typeStr + " - " + current.getDetailTypeTrans());
        time_of_transaction.setText("Thời gian: " + current.getDateTrans());
        description_of_transaction.setText("Ghi chú: " + current.getDescriptionTrans());

        ImageView edit = listItem.findViewById(R.id.edit_trans);
        ImageView delete = listItem.findViewById(R.id.delete_trans);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogEdit(context, current);
                Toast.makeText(context, "Chỉnh sửa giao dịch: " + "1. Ngày: " + current.getDateTrans()
                        + " 2. Số tiền: " + current.getMoneyTrans()
                        + " 3. Loại giao dịch: " + current.getTypeTrans()
                        + " 4. Chi tiết: " + current.getDetailTypeTrans()
                        + " 5. Ghi chú: " + current.getDescriptionTrans()
                        + " 6. Tên chủ tk: " + current.getUserFullname()
                        + " 7. ID giao dịch: " + current.getTransID()
                        + " 8. Tên đăng nhập: " + current.getUserID(), Toast.LENGTH_SHORT).show();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Xóa giao dịch: " + "1. Ngày: " + current.getDateTrans()
                        + " 2. Số tiền: " + current.getMoneyTrans()
                        + " 3. Loại giao dịch: " + current.getTypeTrans()
                        + " 4. Chi tiết: " + current.getDetailTypeTrans()
                        + " 5. Ghi chú: " + current.getDescriptionTrans()
                        + " 6. Tên chủ tk: " + current.getUserFullname()
                        + " 7. ID giao dịch: " + current.getTransID()
                        + " 8. Tên đăng nhập: " + current.getUserID(), Toast.LENGTH_SHORT).show();
            }
        });

        return listItem;
    }

    private void showDialogEdit(Context context, TransModel item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        View dialogView = inflater.inflate(R.layout.dialog_edit_trans, null);
        builder.setView(dialogView);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(R.drawable.login_background);

        Button btnUpdate = dialogView.findViewById(R.id.update_edit);
        Button btnCancel = dialogView.findViewById(R.id.cancel_editing);
        EditText input_money = dialogView.findViewById(R.id.input_money);
        EditText description_trans = dialogView.findViewById(R.id.description_trans);
        AutoCompleteTextView auto_complete = dialogView.findViewById(R.id.auto_complete);
        LinearLayout date_picker_widget = dialogView.findViewById(R.id.date_picker_widget);
        TextView today_or_not = dialogView.findViewById(R.id.today_or_not);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        input_money.setText(item.getMoneyTrans());
        Locale locale = new Locale("en", "US");
        int numDecs = 2;
        TextWatcher watcher = new NumberTextWatcher(input_money, locale, numDecs);
        input_money.addTextChangedListener(watcher);

        description_trans.setText(item.getDescriptionTrans());

        today_or_not.setText(item.getDateTrans());

        auto_complete.setText(item.getDetailTypeTrans());
        auto_complete.requestFocus();
        if (item.getTypeTrans().equals("expense_money")) {
            String[] list = context.getResources().getStringArray(R.array.list_expense);
            DropdownItemAdapter adapter = new DropdownItemAdapter(context, Arrays.asList(list));
            auto_complete.setDropDownBackgroundResource(R.color.color_01);
            auto_complete.setAdapter(adapter);
        } else if (item.getTypeTrans().equals("revenue_money")) {
            String[] list = context.getResources().getStringArray(R.array.list_revenue);
            DropdownItemAdapter adapter = new DropdownItemAdapter(context, Arrays.asList(list));
            auto_complete.setDropDownBackgroundResource(R.color.color_01);
            auto_complete.setAdapter(adapter);
        } else if (item.getTypeTrans().equals("percentage_money")) {
            String[] list = context.getResources().getStringArray(R.array.list_percentage);
            DropdownItemAdapter adapter = new DropdownItemAdapter(context, Arrays.asList(list));
            auto_complete.setDropDownBackgroundResource(R.color.color_01);
            auto_complete.setAdapter(adapter);
        } else if (item.getTypeTrans().equals("loan_money")) {
            String[] list = context.getResources().getStringArray(R.array.list_loan);
            DropdownItemAdapter adapter = new DropdownItemAdapter(context, Arrays.asList(list));
            auto_complete.setDropDownBackgroundResource(R.color.color_01);
            auto_complete.setAdapter(adapter);
        }
        auto_complete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auto_complete.showDropDown();
            }
        });


        Calendar datePicker = Calendar.getInstance();
        date_picker_widget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog endDialog = new DatePickerDialog(context,
                        R.style.CustomDatePickerDialog, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        datePicker.set(Calendar.YEAR, year);
                        datePicker.set(Calendar.MONTH, month);
                        datePicker.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        String selected = DateFormat.getDateInstance(DateFormat.FULL).format(datePicker.getTime());
                        today_or_not.setText(selected);
                    }
                }, datePicker.get(Calendar.YEAR), datePicker.get(Calendar.MONTH), datePicker.get(Calendar.DAY_OF_MONTH));

                endDialog.show();
            }
        });

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = item.getTransID();
                String type = item.getTypeTrans();
                String detailType = auto_complete.getText().toString();
                String money = input_money.getText().toString();
                String date = today_or_not.getText().toString();
                String des = description_trans.getText().toString();
                updateData(Constants.BASE_URL_UPDATE_TRANS_DATA, id, type, detailType, money, date, des);
                dialog.dismiss();
            }
        });



        dialog.show();

    }

    private void updateData(String url, String str1, String str2, String str3, String str4, String str5, String str6) {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest strRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.trim().equals("Success")) {
                            Toast.makeText(context, "CẬP NHẬT THÀNH CÔNG", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(context, "CẬP NHẬT THẤT BẠI", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DEBUG", "SERVER ERROR: " + error.getMessage());
                    }
                }
        ) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("updateIDTrans", str1);
                params.put("updateMainTransType", str2);
                params.put("updateDetailsType", str3);
                params.put("updateMoney", str4);
                params.put("updateDate", str5);
                params.put("updateDes", str6);
                return params;
            }
        };
        queue.add(strRequest);
    }
}
