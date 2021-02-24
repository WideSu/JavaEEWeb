package org.hqu.vibsignal_analysis.mapper;

import org.hqu.vibsignal_analysis.mapper.entity.DataStorage;

import java.util.List;

/**
 * 试验数据表MAPPER接口
 * @author Ricky Zhang
 * @version 2019-3-18
 */
public interface DataStorageMapper {
    void insert(DataStorage data);

    void update(DataStorage data);

    List<DataStorage> findDataIndex(DataStorage dataStorage);

    String findDataPath(DataStorage dataStorage);

    List<DataStorage> findList(DataStorage dataStorage);

    DataStorage selectByPrimaryKey(String uuid);
    //Annie写的查找所有数据表信息
    List<DataStorage> getList(DataStorage dataStorage);
    //Annie按照dataId更新dataName的update
    void updateDataName(DataStorage dataStorage);
    //Annie按照dataId删除的delete
    void delete(DataStorage dataStorage);
}
