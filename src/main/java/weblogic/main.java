package weblogic;


import weblogic.entity.DemoEntity;
import weblogic.service.SpSynTool;

/**
 * Created by Liuh on 2017/1/10.
 */
public class main {

    public static void main(String[] str){
        DemoEntity entity = new DemoEntity();
        for (int i = 0; i < 10; i++){
            new SpSynTool().SpSynCBSTransaction(entity);
        }
    }
}
