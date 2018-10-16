package package1;

import java.util.*;
import java.text.SimpleDateFormat;
import java.io.Serializable;

public class TelephoneStation implements Serializable {
    public enum services {ChangeNumber ,serv1, serv2, serv3}

    protected List<Subscriber> subsList;

    public TelephoneStation() {
        this.subsList = new ArrayList<>();
    }

    public TelephoneStation(List<Subscriber> subsList) {
        this.subsList = new ArrayList<>(subsList);
    }

    public void addNewSubscriber(int num, int money) throws IllegalArgumentException {
        Subscriber newSub = new Subscriber(num, money);
        this.subsList.add(newSub);
    }

    public void timer() {
        for(Subscriber sub : this.subsList) {
            for(Bill bill : sub.bills) {
                bill.date--;
            }
        }
    }

    public void checkAll() {
        for(Subscriber sub : this.subsList) {
            TelephoneStation.Administrator.checkSubscriber(sub);
        }
    }

    public void setSubsList(List<Subscriber> newList) throws IllegalArgumentException {
        if(newList == null) {
            throw new IllegalArgumentException();
        }
        this.subsList = new ArrayList<>(newList);
    }

    public abstract static class Administrator {
        public static void changeNum(Subscriber sub, int newNum) throws IllegalArgumentException, IllegalStateException {
            if(!TelephoneStation.Administrator.checkSubscriber(sub)) {
                throw new IllegalStateException();
            }
            if(newNum <= 0 || newNum == sub.num) {
                throw new IllegalArgumentException ();
            }
            sub.addBill(services.ChangeNumber, 5, 0);
            sub.num = newNum;
        }

        public static void cancelBill(Subscriber sub, int billIndex) throws IndexOutOfBoundsException, IllegalStateException {
            if(!TelephoneStation.Administrator.checkSubscriber(sub)) {
                throw new IllegalStateException();
            }
            if(billIndex < 0 || billIndex >= sub.bills.size()) {
                throw new IndexOutOfBoundsException();
            }
            sub.bills.remove(billIndex);
        }

        public static boolean checkSubscriber(Subscriber sub) {
            for (Bill bill:sub.bills) {
                if(bill.date < 0) {
                    sub.active = false;
                    break;
                }
            }
            return sub.active;
        }
    }

    public class Subscriber implements Serializable {
        private int num;
        private boolean active;
        private int money;
        private List<Bill> bills;
        Date creationDate;

        public Subscriber(int num, int money) throws IllegalArgumentException {
            if(money <= 0 || num <= 0) {
                throw new IllegalArgumentException ();
            }
            this.num = num;
            this.active = true;
            this.money = money;
            this.bills = new ArrayList<>();
            this.creationDate = new Date();
        }

        public void addMoney(int add) throws IllegalArgumentException {
            if(add <= 0) {
                throw new IllegalArgumentException ();
            }
            this.money += add;
        }

        private void subtractMoney(int sub) throws IllegalArgumentException {
            if(sub <= 0 || sub > this.money) {
                throw new IllegalArgumentException ();
            }
            this.money -= sub;
        }

        public void addBill(services service, int payment, int date) throws IllegalArgumentException, IllegalStateException {
            if(!TelephoneStation.Administrator.checkSubscriber(this)) {
                throw new IllegalStateException();
            }
            if(payment <= 0 || date < 0) {
                throw new IllegalArgumentException ();
            }
            Bill newBill = new Bill(service, payment, date);
            this.bills.add(newBill);
        }

        public void addBill(String serviceName, int payment, int date) throws IllegalArgumentException, IllegalStateException {
            addBill(services.valueOf(serviceName), payment, date);
        }

        public void closeBill(int index) throws IndexOutOfBoundsException, IllegalArgumentException {
            if(index < 0 || index >= this.bills.size()) {
                throw new IndexOutOfBoundsException();
            }
            this.subtractMoney(this.bills.get(index).payment);
            this.bills.remove(index);
        }

        public void cancelBill(int index) throws IndexOutOfBoundsException, IllegalStateException {
            TelephoneStation.Administrator.cancelBill(this, index);
        }

        public void changeNum(int newNum) throws IllegalArgumentException, IllegalStateException {
            TelephoneStation.Administrator.changeNum(this, newNum);
        }

        public boolean checkThisSub() {
            return TelephoneStation.Administrator.checkSubscriber(this);
        }

        //public int getBillsListSize() { return this.bills.size(); }

        public String toString() {
            Locale currentLocale = Locale.getDefault();
            ResourceBundle bundle = ResourceBundle.getBundle ("package1", currentLocale);

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

            String info = String.format(bundle.getString("Subscriber_format"),
                    (this.active) ? bundle.getString("Yes") : bundle.getString("No"), this.num, this.money, dateFormat.format(this.creationDate));
            if(this.bills.isEmpty()) {
                info += bundle.getString("Empty");
            }
            else {
                for (Bill bill : this.bills) {
                    info = info.concat(bill.toString());
                }
            }
            return info;
        }
    }

    private class Bill implements Serializable {
        private services service;
        private int payment;
        private int date;

        private Bill(services service, int payment, int date) throws IllegalArgumentException {
            if (payment <= 0 || date < 0) {
                throw new IllegalArgumentException();
            }
            this.service = service;
            this.payment = payment;
            this.date = date;
        }

        public String toString() throws IndexOutOfBoundsException {
            Locale currentLocale = Locale.getDefault();
            ResourceBundle bundle = ResourceBundle.getBundle ("package1", currentLocale);
            return String.format(bundle.getString("Bill_format"),
                    this.service.toString(), this.payment, this.date);
        }
    }
}