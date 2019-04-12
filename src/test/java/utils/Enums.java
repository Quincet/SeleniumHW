package utils;

public class Enums {
    public enum SelectLists{
        Internet{
            public String getNameOfSelector() {
                return "internet";
            }
        },
        Calls{
            public String getNameOfSelector() {
                return "calls";
            }
        },
        Nationality{
            public String getNameOfSelector() {
                return "non_resident";
            }
        };
        public abstract String getNameOfSelector();
    }
    public enum CheckBoxes{
        Messagers{
            public String getXpathOfCheckBox(){
                return "//input[@name='2256' or @name='2050']";
            }
        },
        Music{
            public String getXpathOfCheckBox(){
                return "//input[@name='2255' or @name='2046']";
            }
        },
        SocialNetworks{
            public String getXpathOfCheckBox(){
                return "//input[@name='2258' or @name='2053']";
            }
        },
        Videos{
            public String getXpathOfCheckBox(){
                return "//input[@name='2257' or @name='2047']";
            }
        },
        UnlimitedSms{
            public String getXpathOfCheckBox(){
                return "//input[@name='2333' or @name='2048']";//на форме меняются айдишники чек боксов в зависимости от региона, точно проверено на чекбоксе "безлимитные смс"
            }
        },
        ModemMode{
            public String getXpathOfCheckBox(){
                return "//input[@name='2058']";
            }
        },
        TransferTel{
            public String getXpathOfCheckBox(){
                return "//input[@name='is_mnp']";
            }
        },
        Agreement{
            public String getXpathOfCheckBox(){
                return "//input[@id='agreement']";
            }
        };
        public abstract String getXpathOfCheckBox();
    }
    public enum TextInputs{
        Fio{
            public String getNameOfTextInputArea(){
                return "fio";
            }
        },
        Telephone{
            public String getNameOfTextInputArea(){
                return "phone_mobile";
            }
        },
        Email{
            public String getNameOfTextInputArea(){
                return "email";
            }
        },
        Natyanalyti{
            public String getNameOfTextInputArea(){
                return "temp_non_resident_nationality";
            }
        };
        public abstract String getNameOfTextInputArea();
    }
}