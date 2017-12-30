package com.thinkgem.jeesite.modules.common.service;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;
import com.thinkgem.jeesite.modules.contract.entity.RentContract;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author wangshujin
 */
@Service
@Transactional(readOnly = true)
public class AttachmentService extends CrudService<AttachmentDao, Attachment> {

  /**
   * 根据承租合同、出租合同、物业项目、楼宇、房屋、房间的ID来更新对应的文件路径
   */
  @Transactional(readOnly = false)
  public int updateAttachmentPathByType(Attachment attachment) {
    return dao.updateAttachmentPathByType(attachment);
  }

  @Transactional(readOnly = false)
  public void delete(Attachment attachment) {
    attachment.preUpdate();
    super.delete(attachment);
  }

  @Transactional(readOnly = false)
  public void deleteByTradeIds(List<String> tradingAccountsIds) {
    Attachment attachment = new Attachment();
    attachment.setTradingAccountsIdList(tradingAccountsIds);
    attachment.preUpdate();
    super.delete(attachment);
  }

  @Transactional(readOnly = false)
  public void delRentContract(RentContract rentContract) {
    Attachment attachment = new Attachment();
    attachment.preUpdate();
    attachment.setRentContractId(rentContract.getId());
    super.delete(attachment);
  }
}
