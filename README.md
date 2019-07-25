# city_business_picker_lib
仿京东城市选择城市的弹出框、仿淘宝城市选择公司类型的弹出框

1 仿京东城市选择城市的弹出框

        CityPicker.getInstance().showCityPicker(MainActivity.this, new IPickerListener() {
            @Override
            public void onSelected(BaseBean province, BaseBean city, BaseBean district) {
                StringBuilder sb = new StringBuilder();
                sb.append("省级：").append(province.toString());
                sb.append("\n城市：").append(city != null ? city.toString() : "");
                sb.append("\n区域：").append(district != null ? district.toString() : "");
                mResultTv.setText(sb.toString());
            }

            @Override
            public void onCancel() {
                mResultTv.setText("取消了。");
            }
        });
        
        
2 仿淘宝城市选择公司类型的弹出框

        BusinessPicker.getInstance().showCityPicker(MainActivity.this, new IPickerListener() {
            @Override
            public void onSelected(BaseBean doorType, BaseBean bigType, BaseBean centreType) {
                StringBuilder sb = new StringBuilder();
                sb.append("门类：").append(doorType.toString());
                sb.append("\n大类：").append(bigType != null ? bigType.toString() : "");
                sb.append("\n中类：").append(centreType != null ? centreType.toString() : "");
                mResultTv.setText(sb.toString());
            }

            @Override
            public void onCancel() {
                mResultTv.setText("取消了。");
            }
        });
