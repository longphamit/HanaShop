/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longpc.utils;

import com.longpc.dto.AccountDTO;
import com.longpc.dto.CartDTO;
import com.longpc.dto.ProductDTO;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Details;
import com.paypal.api.payments.Item;
import com.paypal.api.payments.ItemList;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.PayerInfo;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.PaymentExecution;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.base.rest.APIContext;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 *
 * @author ASUS
 */
public class PayPalUtil {
    private static final String CLIENT_ID = "ASBkQcrvee5wDqjQzphbHu9HCDWKAkFkHFjudMdP06R4CjJW_TgSDlc_tzzYA-RSCql9tRFhG78XKQmJ";
    private static final String CLIENT_SECRET = "EELs_ogt_BX_fDraNiMEMiWscbKZkZJVr4JI73eS82UB2_Q0pghakFwNS4Dsh4MrEqrYksYqPz_SLo-Q";
    private static final String MODE = "sandbox";
    private static final String CANCEL_URL="http://localhost:8080/HanaShop/user/paypal/cancel";
    private static final String RETURN_URL="http://localhost:8080/HanaShop/user/paypal/review_payment";

    public String authorizePayment(AccountDTO accountDTO,CartDTO cartDTO) throws Exception {
        Payer payer=getPayerInformation(accountDTO, cartDTO);
        RedirectUrls redirectUrls=getRedirectUrls();
        List<Transaction> listTransactions=getTransactions(cartDTO);
        
        Payment requestPayment=new Payment();
        requestPayment.setTransactions(listTransactions);
        requestPayment.setRedirectUrls(redirectUrls);
        requestPayment.setPayer(payer);
        requestPayment.setIntent("authorize");
       
        APIContext aPIContext=new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        Payment approvedPayment=requestPayment.create(aPIContext);
      
        return getApprovalLink(approvedPayment);
    }

    private Payer getPayerInformation(AccountDTO accountDTO,CartDTO cartDTO) throws Exception { 
        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");
        PayerInfo payerInfo = new PayerInfo();
        payerInfo.setFirstName(accountDTO.getName())
                .setEmail(accountDTO.getEmail());
        payer.setPayerInfo(payerInfo);
        return payer;
    }
    private List<Transaction> getTransactions(CartDTO cartDTO){
        List<Transaction> listTransaction= new ArrayList<>();
        ItemList itemList = new ItemList();
        //tạo amount và detail
        Details details= new Details();
        Amount amount= new Amount();
        amount.setCurrency("USD");
        String total=new String().format("%.2f",cartDTO.total());
        amount.setTotal(total);
        amount.setDetails(details);
        //tạo 1 transaction
        Transaction transaction= new Transaction();
        transaction.setAmount(amount);
        transaction.setDescription("");
        System.out.println(transaction);
        
        //đưa danh sách sản phẩm vào listItem
        List<Item> items= new ArrayList<>();
        for(Map.Entry<String,ProductDTO> entry:cartDTO.getCart().entrySet()){
            Item item= new Item();
            String price=new String().format("%.2f",entry.getValue().getPrice());
            item.setCurrency("USD");
            
            item.setPrice(price);
            item.setQuantity(entry.getValue().getQuantityInCart()+"");
            item.setDescription(entry.getValue().getDescription());
            items.add(item);
        }
        itemList.setItems(items);
        
        //set danh sách sản phẩm lên transaction
        transaction.setItemList(itemList);
        listTransaction.add(transaction);
        return listTransaction;
    }
    private RedirectUrls getRedirectUrls(){
        RedirectUrls redirectUrls= new RedirectUrls();
        redirectUrls.setCancelUrl(CANCEL_URL);
        redirectUrls.setReturnUrl(RETURN_URL);
        return redirectUrls;
    }
    private String getApprovalLink(Payment payment){
        List<Links> links=payment.getLinks();
        String approvalLink=null;
        for(Links link:links){
            if(link.getRel().equals("approval_url")){
                approvalLink=link.getHref();
                break;
            }
        }
        return approvalLink;
    }
    public Payment getPaymentDetail(String paymentID) throws Exception{
        APIContext aPIContext= new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return Payment.get(aPIContext, paymentID);
    }
    public Payment excutePayment(String paymentId,String payerId) throws Exception{
        PaymentExecution paymentExecution= new PaymentExecution();
        paymentExecution.setPayerId(payerId);
        Payment payment= new Payment();
        payment.setId(paymentId);
        APIContext aPIContext= new APIContext(CLIENT_ID, CLIENT_SECRET, MODE);
        return payment.execute(aPIContext, paymentExecution);
    }

}
