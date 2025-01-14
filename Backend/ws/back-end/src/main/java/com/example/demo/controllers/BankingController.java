package com.example.demo.controllers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.models.Banking;
import com.example.demo.repo.BankingRepo;

@RestController
@CrossOrigin("http://localhost:3000")
public class BankingController {
	
	@Autowired
	BankingRepo repo;
	
	@GetMapping("/customers")
	public List<Banking> getAllCustomers(){
		return repo.findAll();
	}
	@PostMapping("/customer")
	public Banking addAccount(@RequestBody Banking b) {
		return repo.save(b);
	}
	@PutMapping("/customer")
	public Banking updateEmployee(@RequestBody Banking b) {
		return repo.save(b);
	}
	@DeleteMapping("/customer/{id}")
	public void deleteEmployee(@PathVariable int id) {
		repo.deleteById(id);
	}
	
	@GetMapping("/customer/{id}")
	public Optional<Banking> getEmpById(@PathVariable int id){
		return repo.findById(id);
	}
	
	@PostMapping("/deposit/{acc_no}/{ifsc}/{amount}")
	public String depositAmount(@PathVariable int acc_no,@PathVariable String ifsc,@PathVariable int amount) {
		
		Banking banking = repo.findByAcc_no(acc_no);
        if (banking != null) {
            int initialAmount = banking.getAmount();
            int newAmount = initialAmount + amount;
            banking.setAmount(newAmount);
            repo.save(banking);
            return "Amount deposited successfully. New balance: " + newAmount;
        } else {
            return "Account not found";
        }
	}
	@PostMapping("/withdrawal/{acc_no}/{ifsc}/{amount}")
	public String withdrawAmount(@PathVariable int acc_no,@PathVariable String ifsc,@PathVariable int amount) {
		
		Banking banking = repo.findByAcc_no(acc_no);
        if (banking != null) {
            int initialAmount = banking.getAmount();
            int newAmount = initialAmount - amount;
            banking.setAmount(newAmount);
            repo.save(banking);
            return "Amount withdrwan successfully. New balance: " + newAmount;
        } else {
            return "Account not found";
        }
	}
	
	@GetMapping("/balance/{acc_no}/{ifsc}")
	public String getBalance(@PathVariable int acc_no,@PathVariable String ifsc) {
		Banking banking = repo.findByAcc_no(acc_no);
        if (banking != null) {
           int currAmount = banking.getAmount();
            return "Current balance: " + currAmount;
        } else {
            return "Account not found";
        }
	}
	
	@PostMapping("/chequeDeposit/{fromAcc}/{fromIFSC}/{toAcc}/{toIFSC}/{amount}")
	public String depositCheque(
	        @PathVariable int fromAcc, 
	        @PathVariable String fromIFSC, 
	        @PathVariable int toAcc, 
	        @PathVariable String toIFSC,
	        @PathVariable int amount
	    ) {
	        Banking fromAccount = repo.findByAcc_noAndIfsc(fromAcc, fromIFSC);
	        Banking toAccount = repo.findByAcc_noAndIfsc(toAcc, toIFSC);
	        
	        if (fromAccount == null) {
	            return "Source account not found";
	        }
	        if (toAccount == null) {
	            return "Destination account not found";
	        }
	        
	        int fromAccountBalance = fromAccount.getAmount();
	        if (fromAccountBalance >= amount) {
	            int toAccountBalance = toAccount.getAmount();
	            fromAccount.setAmount(fromAccountBalance - amount);
	            toAccount.setAmount(toAccountBalance + amount);
	            
	            repo.save(fromAccount);
	            repo.save(toAccount);
	            
	            return "Cheque deposited successfully. Source account balance: " + fromAccount.getAmount() +
	                   ", Destination account balance: " + toAccount.getAmount();
	        } else {
	            return "Insufficient balance in the source account";
	        }
	    }
	
}
