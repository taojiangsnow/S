function [home_ids,number_user] = ReadData()
alldata = open('F:\realitymining.mat');
a = alldata.s;
number_user = size(a,2);
max_num_homeids = 10;
home_ids = zeros(number_user,max_num_homeids);
for i = 1:number_user
temp_homeid = [];
    templocs = a(i).locs;
    length = size(templocs,1);
    
    %the user does not upload any location context
    if (length == 0)
        continue;
    end
    
    locids = a(i).loc_ids;
    num_homeids = numel(a(i).home_ids);
    if (num_homeids ~= 0)
        temp_homeid = zeros(1,num_homeids);
    end
    for m = 1:num_homeids
        temp_homeid(m) = a(i).home_ids(m);
    end
    
    output = zeros(length,8);
    output(:,8) = locids;
    for j = 1:length
        temp_datevec = datevec(templocs(j,1));
        length_date = numel(temp_datevec);
        if (length_date == 6)
            for k = 1:6
            	output(j,k) = temp_datevec(k);                            
            end
        end
        output(j,7) = templocs(j,2);
        index_homeid = find(output(j,7)==temp_homeid);
        if(numel(index_homeid) ~= 0)
            temp_homeid(index_homeid) = locids(j);
        end
    end
    for j = 1:num_homeids
        home_ids(i,j) = temp_homeid(j);
    end
    %dlmwrite(['F:\result\',num2str(i),'_',num2str(length),'.txt'],output,'delimiter',' ','newline','pc');
end
dlmwrite('F:\homeids.txt',home_ids,'delimiter',' ','newline','pc');
%END

