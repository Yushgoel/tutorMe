package io.agora.tutorials1v1vcall;

import java.util.ArrayList;

public interface IAPIResponse {

    void onSuccessData(ArrayList<Data> data1);
    void onSuccessInt(int id);
}
