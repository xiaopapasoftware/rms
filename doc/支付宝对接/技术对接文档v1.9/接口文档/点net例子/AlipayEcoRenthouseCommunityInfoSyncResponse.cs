using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Xml.Serialization;

namespace Aop.Api.Response
{
    public class AlipayEcoRenthouseCommunityInfoSyncResponse : AopResponse
    {
        /// <summary>
        /// 小区同步请求号
        /// </summary>
        [XmlElement("comm_req_id")]
        public string Comm_req_id { get; set; }

        /// <summary>
        /// 小区同步请求号
        /// </summary>
        [XmlElement("status")]
        public int Status { get; set; }
    }
}
