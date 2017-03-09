package com.thinkgem.jeesite.modules.common.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.thinkgem.jeesite.common.service.CrudService;
import com.thinkgem.jeesite.modules.common.dao.AttachmentDao;
import com.thinkgem.jeesite.modules.common.entity.Attachment;

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
    dao.delete(attachment);
  }

}
