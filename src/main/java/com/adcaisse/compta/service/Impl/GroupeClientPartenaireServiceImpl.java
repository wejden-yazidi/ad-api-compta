package com.adcaisse.compta.service.Impl;

import com.adcaisse.compta.Enum.EnumMessage;
import com.adcaisse.compta.repository.IGroupeClientPartenaireRepository;
import com.adcaisse.compta.response.ResponseObject;
import com.adcaisse.compta.service.IGroupeClientPartenaireService;
import com.bprice.persistance.model.GroupeClientPartenaire;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GroupeClientPartenaireServiceImpl implements IGroupeClientPartenaireService {
    Logger logger = LoggerFactory.getLogger(GroupeClientPartenaireServiceImpl.class);

    @Autowired
    private IGroupeClientPartenaireRepository groupeClientPartenaireRepository;
    @Override
    public ResponseObject CreateGroupeClientPartenaire(GroupeClientPartenaire groupeClientPartenaire) {
        try{
            if(groupeClientPartenaire!=null){
                GroupeClientPartenaire groupe=groupeClientPartenaireRepository.save(groupeClientPartenaire);
                logger.info("GroupeClientPartenaireServiceImpl : CreateGroupeClientPartenaire : SUCCESS_CREATION : " + groupe.getIdGroupeClientPartenaire());
                return new ResponseObject(EnumMessage.SUCCESS_CREATION.code, EnumMessage.SUCCESS_CREATION.label, groupe);
            }else{
                logger.info("GroupeClientPartenaireServiceImpl : CreateGroupeClientPartenaire : EMPTY_OBJECT " );
                return new ResponseObject(EnumMessage.EMPTY_OBJECT.code, EnumMessage.EMPTY_OBJECT.label, null);
            }


        }catch (Exception e){
            logger.info("GroupeClientPartenaireServiceImpl : CreateGroupeClientPartenaire : Exception : " + e.getMessage());
            return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
        }
    }

    @Override
    public ResponseObject actifGroupeClientPartenaire(String idgroupeClientPartenaire, Short type) {
        try{
            if(idgroupeClientPartenaire!=null && type!=null){
                GroupeClientPartenaire groupe=groupeClientPartenaireRepository.findByIdGroupeClientPartenaire(idgroupeClientPartenaire);
                if(groupe!=null){
                    if(type==1){
                        groupe.setIsActif((short)1);
                        GroupeClientPartenaire groupenew=groupeClientPartenaireRepository.save(groupe);

                        logger.info("GroupeClientPartenaireServiceImpl : actifGroupeClientPartenaire : SUCCESS_UPDATE : " + groupenew.getIdGroupeClientPartenaire());
                        return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, groupenew);
                    }else if(type==0){
                        groupe.setIsActif((short)0);
                        GroupeClientPartenaire groupenew=groupeClientPartenaireRepository.save(groupe);

                        logger.info("GroupeClientPartenaireServiceImpl : actifGroupeClientPartenaire : SUCCESS_UPDATE : " + groupenew.getIdGroupeClientPartenaire());
                        return new ResponseObject(EnumMessage.SUCCESS_UPDATE.code, EnumMessage.SUCCESS_UPDATE.label, groupenew);
                    }else{
                        logger.info("GroupeClientPartenaireServiceImpl : actifGroupeClientPartenaire : UNKNOWN_TYPE " );
                        return new ResponseObject(EnumMessage.UNKNOWN_TYPE.code, EnumMessage.UNKNOWN_TYPE.label, null);
                    }
                }else{
                    logger.info("GroupeClientPartenaireServiceImpl : actifGroupeClientPartenaire : GROUPE_NOT_EXIST " );
                    return new ResponseObject(EnumMessage.GROUPE_NOT_EXIST.code, EnumMessage.GROUPE_NOT_EXIST.label, null);
                }

            }else{
                logger.info("GroupeClientPartenaireServiceImpl : actifGroupeClientPartenaire : EMPTY_OBJECT " );
                return new ResponseObject(EnumMessage.PARAMETRES_EMPTY.code, EnumMessage.PARAMETRES_EMPTY.label, null);
            }


        }catch (Exception e){
            logger.info("GroupeClientPartenaireServiceImpl : actifGroupeClientPartenaire : Exception : " + e.getMessage());
            return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
        }
    }

    @Override
    public ResponseObject findAllByIdPartenaireOrderByDateCreationDesc(String idpartenaire) {
        try {
            if(idpartenaire!=null){
                List<GroupeClientPartenaire> groupeClientPartenaires=groupeClientPartenaireRepository.findAllByIdPartenaireOrderByDateCreationDesc(idpartenaire);
                if(groupeClientPartenaires.size()>0){
                    return new ResponseObject(EnumMessage.LIST_NOT_EMPTY.code, EnumMessage.LIST_NOT_EMPTY.label, groupeClientPartenaires);
                }else{
                    return new ResponseObject(EnumMessage.LIST_EMPTY.code, EnumMessage.LIST_EMPTY.label, null);

                }
            }else{
                logger.info("GroupeClientPartenaireServiceImpl : findAllByIdPartenaireOrderByDateCreationDesc : ID_EMPTY");
                return new ResponseObject(EnumMessage.ID_EMPTY.code, EnumMessage.ID_EMPTY.label, null);
            }
        }catch (Exception e){
            logger.info("GroupeClientPartenaireServiceImpl : findAllByIdPartenaireOrderByDateCreationDesc : Exception : " + e.getMessage());
            return new ResponseObject(EnumMessage.ERREUR_QUERY.code, e.getMessage(), null);
        }
    }
}
